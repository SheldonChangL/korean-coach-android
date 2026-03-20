package com.koreancoach.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.koreancoach.app.data.curriculum.CurriculumBootstrapper
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.domain.model.ThemeMode
import com.koreancoach.app.domain.model.UiLanguage
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import com.koreancoach.app.ui.KoreanCoachNavHost
import com.koreancoach.app.ui.theme.KoreanCoachTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var prefsDataStore: UserPreferencesDataStore
    @Inject lateinit var speechPlaybackService: SpeechPlaybackService
    @Inject lateinit var curriculumBootstrapper: CurriculumBootstrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            prefsDataStore.onboardingData
                .map { it.uiLanguage }
                .distinctUntilChanged()
                .collect { language ->
                    val localeTags = when (language) {
                        UiLanguage.TRADITIONAL_CHINESE -> "zh-TW"
                        UiLanguage.ENGLISH -> "en"
                    }
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeTags))
                    curriculumBootstrapper.ensureSeeded()
                }
        }
        setContent {
            val themeMode by prefsDataStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val isSystemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemDark
            }
            KoreanCoachTheme(darkTheme = darkTheme) {
                KoreanCoachNavHost()
            }
        }
    }

    override fun onDestroy() {
        if (isFinishing) {
            speechPlaybackService.shutdown()
        }
        super.onDestroy()
    }
}
