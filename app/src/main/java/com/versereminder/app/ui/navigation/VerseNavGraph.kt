package com.versereminder.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.versereminder.app.VerseReminderApp
import com.versereminder.app.domain.model.UserSession
import com.versereminder.app.ui.auth.AuthScreen
import com.versereminder.app.ui.auth.AuthViewModel
import com.versereminder.app.ui.bookmarks.BookmarkScreen
import com.versereminder.app.ui.bookmarks.BookmarkViewModel
import com.versereminder.app.ui.components.SpiritualLoadingView
import com.versereminder.app.ui.history.HistoryScreen
import com.versereminder.app.ui.history.HistoryViewModel
import com.versereminder.app.ui.home.HomeScreen
import com.versereminder.app.ui.home.HomeViewModel
import com.versereminder.app.ui.schedule.ScheduleScreen
import com.versereminder.app.ui.schedule.ScheduleViewModel
import com.versereminder.app.ui.settings.SettingsScreen
import com.versereminder.app.ui.settings.SettingsViewModel
import com.versereminder.app.ui.theme.FigmaDarkBg
import com.versereminder.app.ui.theme.FigmaDarkBorder
import com.versereminder.app.ui.theme.FigmaGold
import com.versereminder.app.ui.theme.FigmaGoldText
import com.versereminder.app.ui.theme.FigmaMuted
import com.versereminder.app.ui.theme.FigmaNavBg

@Composable
fun VerseNavGraph(
    app: VerseReminderApp
) {
    // Fast session resolution with initial null to accurately detect login/guest state
    val userSessionState by app.authRepository.userSessionFlow.collectAsState(initial = null)

    val session = userSessionState
    if (session == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FigmaDarkBg),
            contentAlignment = Alignment.Center
        ) {
            SpiritualLoadingView()
        }
    } else {
        VerseNavContent(app = app, userSession = session)
    }
}

@Composable
private fun VerseNavContent(
    app: VerseReminderApp,
    userSession: UserSession
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val initialDestination = if (userSession.isLoggedIn || userSession.isGuest) {
        Screen.Home.route
    } else {
        Screen.Auth.route
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Auth.route) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Screen.bottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = FigmaGoldText,
                                selectedTextColor = FigmaGoldText,
                                unselectedIconColor = FigmaMuted,
                                unselectedTextColor = FigmaMuted,
                                indicatorColor = FigmaGold.copy(alpha = 0.18f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = initialDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Auth.route) {
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModel.Factory(
                        authRepository = app.authRepository
                    )
                )
                AuthScreen(
                    viewModel = authViewModel,
                    onAuthSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.Factory(
                        verseRepository = app.verseRepository,
                        scheduleRepository = app.scheduleRepository,
                        settingsRepository = app.settingsRepository
                    )
                )
                val readingPlanViewModel: com.versereminder.app.ui.readingplan.ReadingPlanViewModel = viewModel(
                    factory = com.versereminder.app.ui.readingplan.ReadingPlanViewModel.Factory(
                        readingPlanRepository = app.readingPlanRepository,
                        settingsRepository = app.settingsRepository
                    )
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    readingPlanViewModel = readingPlanViewModel,
                    onNavigateToSchedule = {
                        navController.navigate(Screen.Schedule.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.Schedule.route) {
                val scheduleViewModel: ScheduleViewModel = viewModel(
                    factory = ScheduleViewModel.Factory(
                        scheduleRepository = app.scheduleRepository
                    )
                )
                ScheduleScreen(viewModel = scheduleViewModel)
            }

            composable(Screen.Bookmarks.route) {
                val bookmarkViewModel: BookmarkViewModel = viewModel(
                    factory = BookmarkViewModel.Factory(
                        verseRepository = app.verseRepository,
                        settingsRepository = app.settingsRepository
                    )
                )
                BookmarkScreen(viewModel = bookmarkViewModel)
            }

            composable(Screen.History.route) {
                val historyViewModel: HistoryViewModel = viewModel(
                    factory = HistoryViewModel.Factory(
                        verseRepository = app.verseRepository,
                        settingsRepository = app.settingsRepository,
                        onClearHistory = {
                            app.database.historyDao().clearHistory()
                        }
                    )
                )
                HistoryScreen(viewModel = historyViewModel)
            }

            composable(Screen.Settings.route) {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModel.Factory(
                        settingsRepository = app.settingsRepository,
                        scheduleRepository = app.scheduleRepository,
                        verseRepository = app.verseRepository,
                        authRepository = app.authRepository,
                        alarmScheduler = app.alarmScheduler,
                        context = app
                    )
                )
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateToAuth = {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
