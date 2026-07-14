package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val _themeLiveData = MutableLiveData(false)
    fun observeTheme(): LiveData<Boolean> = _themeLiveData

    init {
        _themeLiveData.value = themeInteractor.getTheme()
    }

    fun changeThemeApp(isDark: Boolean) {
        themeInteractor.putTheme(isDark)
        _themeLiveData.postValue(isDark)
    }

    fun openCourse() {
        sharingInteractor.shareApp()
    }

    fun contactSupportToEmail() {
        sharingInteractor.openSupport()
    }

    fun openOffer() {
        sharingInteractor.openTerms()
    }
}