package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application() {
    val themeSwitcher: ThemeRepository by inject()
    val darkTheme: Boolean
        get() = themeSwitcher.getTheme()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                Creator.getModules()
            )
        }
        applyTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnable: Boolean) {
        applyTheme(darkThemeEnable)
    }

    private fun applyTheme(darkThemeEnable: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnable) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}