package com.versereminder.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.versereminder.app.data.local.dao.BookmarkDao
import com.versereminder.app.data.local.dao.HistoryDao
import com.versereminder.app.data.local.dao.ReadingPlanDao
import com.versereminder.app.data.local.dao.ScheduleDao
import com.versereminder.app.data.local.dao.VerseDao
import com.versereminder.app.data.local.entity.BookmarkEntity
import com.versereminder.app.data.local.entity.HistoryEntity
import com.versereminder.app.data.local.entity.ReadingPlanProgressEntity
import com.versereminder.app.data.local.entity.ScheduleEntity
import com.versereminder.app.data.local.entity.VerseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader

import androidx.room.migration.Migration

@Database(
    entities = [
        VerseEntity::class,
        ScheduleEntity::class,
        BookmarkEntity::class,
        HistoryEntity::class,
        ReadingPlanProgressEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun verseDao(): VerseDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
    abstract fun readingPlanDao(): ReadingPlanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE verses ADD COLUMN isCustom INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Ensure table schema is 100% compliant with Room v3 schema
                db.execSQL("CREATE TABLE IF NOT EXISTS `verses_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `book` TEXT NOT NULL, `chapter` INTEGER NOT NULL, `verse` INTEGER NOT NULL, `reference` TEXT NOT NULL, `textId` TEXT NOT NULL, `textEn` TEXT NOT NULL, `category` TEXT NOT NULL, `themeTag` TEXT NOT NULL, `isCustom` INTEGER NOT NULL DEFAULT 0)")
                try {
                    db.execSQL("INSERT INTO `verses_temp` (id, book, chapter, verse, reference, textId, textEn, category, themeTag, isCustom) SELECT id, book, chapter, verse, reference, textId, textEn, category, themeTag, coalesce(isCustom, 0) FROM `verses`")
                } catch (_: Exception) {
                    try {
                        db.execSQL("INSERT INTO `verses_temp` (id, book, chapter, verse, reference, textId, textEn, category, themeTag, isCustom) SELECT id, book, chapter, verse, reference, textId, textEn, category, themeTag, 0 FROM `verses`")
                    } catch (_: Exception) {}
                }
                db.execSQL("DROP TABLE IF EXISTS `verses`")
                db.execSQL("ALTER TABLE `verses_temp` RENAME TO `verses`")
            }
        }

        private val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE verses ADD COLUMN isCustom INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `reading_plan_progress` (" +
                            "`planId` TEXT NOT NULL PRIMARY KEY, " +
                            "`completedDays` TEXT NOT NULL, " +
                            "`currentDay` INTEGER NOT NULL, " +
                            "`lastReadTimestamp` INTEGER NOT NULL, " +
                            "`isCompleted` INTEGER NOT NULL DEFAULT 0)"
                )
            }
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "verse_reminder.db"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3, MIGRATION_3_4)
                .fallbackToDestructiveMigration()
                .fallbackToDestructiveMigrationOnDowngrade()
                .addCallback(DatabaseCallback(context.applicationContext, scope))
                .build()
        }

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    val db = buildDatabase(context, scope)
                    // Trigger schema verification synchronously
                    db.openHelper.writableDatabase
                    INSTANCE = db
                    db
                } catch (e: Throwable) {
                    e.printStackTrace()
                    try {
                        context.applicationContext.deleteDatabase("verse_reminder.db")
                    } catch (_: Exception) {}
                    val cleanDb = buildDatabase(context, scope)
                    INSTANCE = cleanDb
                    cleanDb
                }
            }
        }

        private class DatabaseCallback(
            private val context: Context,
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(context, database)
                    }
                }
            }
        }

        suspend fun populateInitialData(context: Context, database: AppDatabase) {
            try {
                // 1. Populate verses from assets if not already populated
                if (database.verseDao().getVerseCount() == 0) {
                    val inputStream = context.assets.open("seed_verses.json")
                    val reader = InputStreamReader(inputStream)
                    val listType = object : TypeToken<List<VerseEntity>>() {}.type
                    val verses: List<VerseEntity> = Gson().fromJson(reader, listType)
                    reader.close()

                    if (verses.isNotEmpty()) {
                        database.verseDao().insertVerses(verses)
                    }
                }

                // 2. Populate default schedules (Pagi 06:00, Siang 12:00, Malam 19:00)
                if (database.scheduleDao().getCount() == 0) {
                    val defaultSchedules = listOf(
                        ScheduleEntity(
                            id = 1,
                            title = "Ayat Pagi",
                            hour = 6,
                            minute = 0,
                            category = "PEACE",
                            isEnabled = true
                        ),
                        ScheduleEntity(
                            id = 2,
                            title = "Penguatan Siang",
                            hour = 12,
                            minute = 0,
                            category = "STRENGTH",
                            isEnabled = true
                        ),
                        ScheduleEntity(
                            id = 3,
                            title = "Renungan Malam",
                            hour = 19,
                            minute = 0,
                            category = "WISDOM",
                            isEnabled = true
                        )
                    )
                    database.scheduleDao().insertSchedules(defaultSchedules)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
