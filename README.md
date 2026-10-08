# Verse Reminder 📖✨

Aplikasi Android Native modern bernuansa spiritual Alkitabiah yang elegan dengan presisi desain Figma bertema gelap dan aksen emas (*Dark & Gold Aesthetics*). Dirancang 100% *offline-first*, cepat, aman, dan hemat baterai untuk membantu pengguna membaca, merenungkan, dan mengingat firman Tuhan setiap hari.

---

## 🌟 Fitur Utama

### 1. 🎨 Desain Antarmuka Figma ("Verse Reminder 1")
- **Palet Warna & Tipografi Eksklusif**: Menggunakan token desain Figma (`#090B10` latar belakang gelap pekat, `#12151F` kontainer kartu, `#D4AF37` aksen emas mulia, dan `#EADBB6` teks nats firman).
- **Tipografi Alkitabiah**: Font Serif anggun untuk perenungan firman, dengan pratinjau instan skala font (0.85x – 1.40x) dan 4 gaya tipografi (Serif, Sans, Rounded, Monospace).
- **Animasi Spiritual Halus**: Indikator loading spiritual berbasis akselerasi GPU tanpa jank/lag.
- **Dukungan Mode Tampilan**: Mode Terang (*Light*), Mode Gelap (*Dark*), dan Otomatis mengikuti sistem ponsel.

### 2. 📚 8 Versi Terjemahan Alkitab
- **Bahasa Indonesia**:
  - **TB** (Terjemahan Baru)
  - **TB2** (Terjemahan Baru Edisi 2)
  - **BIS** (Bahasa Indonesia Sehari-hari / BIMK)
  - **TSI** (Terjemahan Sederhana Indonesia)
- **Bahasa Inggris**:
  - **WEB** (World English Bible)
  - **KJV** (King James Version)
  - **NIV** (New International Version)
  - **ESV** (English Standard Version)

### 3. 🔊 Text-to-Speech (TTS) Narasi Suara Firman
- Pembacaan ayat native Android tanpa ketergantungan library berat eksternal.
- Pengenalan bahasa otomatis: aksen Indonesia (`id_ID`) untuk versi TB/TB2/BIS/TSI dan aksen Inggris (`en_US`) untuk versi WEB/KJV/NIV/ESV.
- Tempo pembacaan kontemplatif (`0.92f`) dengan tombol interaktif Play/Stop di seluruh kartu ayat.

### 4. 🖼️ Canvas Image Card Generator (Format Story & Feed)
- Generator gambar HD native Android Canvas bergaya kartu Figma.
- Pilihan format instan melalui modal berbagi:
  - 📱 **Story (9:16 - 1080x1920)**: Format pas untuk Instagram Story dan Status WhatsApp.
  - 🖼️ **Post Persegi (1:1 - 1080x1080)**: Format pas untuk Feed Instagram dan media sosial.
  - ✍️ **Teks Firman**: Salinan nats dan referensi langsung.
- Menggunakan Android `FileProvider` (`content://`) yang aman sesuai standar Android 7.0–15.

### 5. 🔍 Pencarian Cepat Firman Alkitab (Instant Search)
- Kolom pencarian responsif di Layar Beranda dengan *debounce 300ms* untuk mencegah lag.
- Pencarian cerdas multi-kolom mencakup nama kitab, teks Bahasa Indonesia, teks Bahasa Inggris, kategori tema, serta **ayat kustom buatan pengguna**.

### 6. 📖 Rencana Baca Alkitab Tematik Harian (Devotional Reading Plan)
- 4 paket perjalanan renungan offline-first:
  1. 🕊️ **7 Hari Menemukan Damai Sejahtera** (Mengatasi stres & kecemasan pikiran)
  2. 🛡️ **7 Hari Mengatasi Rasa Takut & Khawatir** (Kekuatan dari janji Allah)
  3. ❤️ **14 Hari Berakar Dalam Kasih Kristus** (Kasih, pengampunan, & rekonsiliasi)
  4. 💡 **7 Hari Hikmat & Petunjuk Hidup** (Kitab Amsal & panduan keputusan hidup)
- Konten renungan harian lengkap: Teks Nats (+ Audio TTS), Renungan Aplikatif (2–3 paragraf), Doa Harian, dan Tombol Centang *"Tandai Selesai"*.
- Banner pelacak progres di Beranda dengan progress bar persentase emas.

### 7. ✍️ Fitur Tambah & Kelola Ayat Pribadi (User Custom Verses)
- Pengguna dapat menambahkan ayat hafalan atau nats favorit sendiri dengan referensi, isi firman, dan kategori tema.
- Dilengkapi badge emas khusus `PRIBADI`, filter chip `✍️ Ayat Pribadi`, serta opsi dialog konfirmasi hapus permanen.

### 8. ⏰ Sistem Pengingat & Notifikasi Multi-Channel
- Sinkronisasi penuh antara sakelar setelan dan `AlarmManager`.
- 4 Kanal Notifikasi Android (Default, Getar Saja, Suara Saja, Senyap).
- Format pratinjau: Lengkap (*FULL*), Ringkas (*SHORT*), atau Referensi Saja (*REF_ONLY*).
- `BootReceiver` otomatis memulihkan jadwal alarm setelah ponsel di-restart.
- Aksi interaktif langsung di bilah notifikasi: *"Simpan ke Bookmark"* dan *"Bagikan"*.

### 9. 📱 Home Screen App Widget (Jetpack Glance)
- Widget firman Tuhan interaktif di layar beranda ponsel yang otomatis tersinkronisasi saat jadwal notifikasi berbunyi.

### 10. 👤 Fleksibilitas Akun & Mode Tamu
- Login via Email & Password dengan verifikasi kode 6-digit.
- Login instan via Akun Google.
- Mode Tamu (*Guest Mode*) untuk penggunaan penuh tanpa registrasi.
- Transisi sesi aman tanpa menghapus data ayat lokal perangkat saat logout.

---

## 🛠️ Arsitektur & Teknologi

- **Bahasa**: Kotlin 2.0 (JVM 21)
- **UI Framework**: Jetpack Compose (Material 3) + Jetpack Navigation
- **Database Lokal**: SQLite via Android Room Database (v4) dengan migrasi aman & *self-healing recovery*
- **Penyimpanan Preferensi**: Android DataStore Preferences
- **Komponen Home Widget**: Jetpack Glance 1.1.0
- **Audio Engine**: Android Native TextToSpeech (`TextToSpeech.OnInitListener`)
- **Visual Card Generator**: Android 2D Graphics Canvas & StaticLayout
- **Background Scheduler**: Android `AlarmManager` (`RTC_WAKEUP`) + `BroadcastReceiver`
- **Keamanan Berkas**: AndroidX `FileProvider` dengan *scoped cache path*
- **Kompatibilitas**: Android 8.0 Oreo (API 26) hingga Android 15 (API 35)

---

## 🧪 Pengujian & Kualitas Kode

Proyek ini dilengkapi 38 automated unit tests dengan tingkat keberhasilan **100% Lulus (0 Failures, 0 Errors)**:
- `ComprehensiveChecklistAndSecurityTest`: 19 pengujian
- `Phase3SearchAndPlanTest`: 4 pengujian
- `Phase2MediaIntegrationTest`: 4 pengujian
- `SettingsAlarmIntegrationTest`: 4 pengujian
- `VerseModelTest`: 5 pengujian
- `ScheduleModelTest`: 2 pengujian

---

## 🚀 Cara Menjalankan & Membangun Proyek

1. **Clone repository ini**:
   ```bash
   git clone https://github.com/StevenHarun/verse-reminder.git
   cd verse-reminder
   ```

2. **Buka di Android Studio** (Ladybug / Koala atau lebih baru).

3. **Jalankan Unit Test**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Kompilasi Berkas APK Debug**:
   ```bash
   ./gradlew assembleDebug
   ```
   *Berkas APK hasil build berada di `app/build/outputs/apk/debug/app-debug.apk` atau `VerseReminder.apk`.*

---

## 📄 Lisensi

Hak Cipta © 2026 Steven Harun. Seluruh hak cipta dilindungi undang-undang.
