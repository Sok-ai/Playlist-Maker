package com.example.playlistmaker.sharing.domain.api

interface Navigator {
    fun shareLink()
    fun sendEmail()
    fun openUrl()
}