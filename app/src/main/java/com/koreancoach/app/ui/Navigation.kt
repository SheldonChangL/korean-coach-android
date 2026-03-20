package com.koreancoach.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.koreancoach.app.ui.feature.analytics.AnalyticsScreen
import com.koreancoach.app.ui.feature.dashboard.DashboardScreen
import com.koreancoach.app.ui.feature.flashcard.FlashCardScreen
import com.koreancoach.app.ui.feature.hangul.HangulExploreScreen
import com.koreancoach.app.ui.feature.hangul.HangulPathScreen
import com.koreancoach.app.ui.feature.hangul.HangulStageScreen
import com.koreancoach.app.ui.feature.lesson.LessonDetailScreen
import com.koreancoach.app.ui.feature.lessons.LessonListScreen
import com.koreancoach.app.ui.feature.onboarding.OnboardingScreen
import com.koreancoach.app.ui.feature.pronunciation.PronunciationPracticeScreen
import com.koreancoach.app.ui.feature.quiz.QuizScreen
import com.koreancoach.app.ui.feature.review.ReviewScreen
import com.koreancoach.app.ui.feature.settings.SettingsScreen
import com.koreancoach.app.ui.feature.writing.HangulWritingScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object LessonList : Screen("lessons")
    object LessonDetail : Screen("lesson/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson/$lessonId"
    }
    object FlashCard : Screen("flashcard/{lessonId}") {
        fun createRoute(lessonId: String) = "flashcard/$lessonId"
    }
    object HangulPath : Screen("hangul_path")
    object HangulExplore : Screen("hangul_explore")
    object HangulStage : Screen("hangul_stage/{lessonId}") {
        fun createRoute(lessonId: String) = "hangul_stage/$lessonId"
    }
    object Quiz : Screen("quiz/{lessonId}") {
        fun createRoute(lessonId: String) = "quiz/$lessonId"
    }
    object PronunciationPractice : Screen("pronunciation/{lessonId}") {
        fun createRoute(lessonId: String) = "pronunciation/$lessonId"
    }
    object HangulWriting : Screen("hangul_writing?characterId={characterId}") {
        fun createRoute(characterId: String? = null) =
            if (characterId.isNullOrBlank()) "hangul_writing" else "hangul_writing?characterId=$characterId"
    }
    object Review : Screen("review")
    object Settings : Screen("settings")
    object Analytics : Screen("analytics")
}

@Composable
fun KoreanCoachNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    fun navigateToHangulPath() {
        if (!navController.popBackStack(Screen.HangulPath.route, false)) {
            navController.navigate(Screen.HangulPath.route) {
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it / 2 }) },
        exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it / 2 }) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -it / 2 }) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { it / 2 }) }
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = { startWithHangul ->
                    navController.navigate(
                        if (startWithHangul) Screen.HangulPath.route else Screen.Dashboard.route
                    ) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToLessons = { navController.navigate(Screen.LessonList.route) },
                onNavigateToReview = { navController.navigate(Screen.Review.route) },
                onNavigateToLesson = { id -> navController.navigate(Screen.LessonDetail.createRoute(id)) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) },
                onNavigateToHangulWriting = { navController.navigate(Screen.HangulWriting.createRoute()) },
                onNavigateToHangulPath = { navController.navigate(Screen.HangulPath.route) },
                onNavigateToHangulExplore = { navController.navigate(Screen.HangulExplore.route) }
            )
        }
        composable(Screen.HangulPath.route) {
            HangulPathScreen(
                showBackButton = navController.previousBackStackEntry != null,
                onBack = { navController.popBackStack() },
                onExplore = { navController.navigate(Screen.HangulExplore.route) },
                onOpenLesson = { lessonId -> navController.navigate(Screen.HangulStage.createRoute(lessonId)) }
            )
        }
        composable(Screen.HangulExplore.route) {
            HangulExploreScreen(
                showBackButton = navController.previousBackStackEntry != null,
                onBack = {
                    if (!navController.popBackStack()) {
                        navigateToHangulPath()
                    }
                },
                onOpenStage = { lessonId -> navController.navigate(Screen.HangulStage.createRoute(lessonId)) }
            )
        }
        composable(
            route = Screen.HangulStage.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStack ->
            val lessonId = backStack.arguments?.getString("lessonId") ?: return@composable
            HangulStageScreen(
                showBackButton = navController.previousBackStackEntry != null,
                onBack = {
                    if (!navController.popBackStack()) {
                        navigateToHangulPath()
                    }
                },
                onBackToPath = { navigateToHangulPath() },
                onOpenWriting = { characterId ->
                    navController.navigate(Screen.HangulWriting.createRoute(characterId))
                }
            )
        }
        composable(Screen.LessonList.route) {
            LessonListScreen(
                onLessonClick = { id -> navController.navigate(Screen.LessonDetail.createRoute(id)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStack ->
            val lessonId = backStack.arguments?.getString("lessonId") ?: return@composable
            LessonDetailScreen(
                lessonId = lessonId,
                onStartFlashCards = { navController.navigate(Screen.FlashCard.createRoute(lessonId)) },
                onStartQuiz = { navController.navigate(Screen.Quiz.createRoute(lessonId)) },
                onStartPronunciation = { navController.navigate(Screen.PronunciationPractice.createRoute(lessonId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.FlashCard.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStack ->
            val lessonId = backStack.arguments?.getString("lessonId") ?: return@composable
            FlashCardScreen(
                lessonId = lessonId,
                onFinish = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStack ->
            val lessonId = backStack.arguments?.getString("lessonId") ?: return@composable
            QuizScreen(
                lessonId = lessonId,
                onFinish = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.LessonList.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.PronunciationPractice.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStack ->
            val lessonId = backStack.arguments?.getString("lessonId") ?: return@composable
            PronunciationPracticeScreen(
                lessonId = lessonId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.HangulWriting.route,
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            HangulWritingScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Review.route) {
            ReviewScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(onBack = { navController.popBackStack() })
        }
    }
}
