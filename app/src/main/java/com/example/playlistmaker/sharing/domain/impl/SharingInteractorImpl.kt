package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.Navigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val navigate: Navigator) : SharingInteractor {
    override fun shareApp() {
        navigate.shareLink()
    }

    override fun openSupport() {
        navigate.sendEmail()
    }

    override fun openTerms() {
        navigate.openUrl()
    }
}