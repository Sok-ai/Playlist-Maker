package com.example.playlistmaker.library.ui.view_model

sealed interface AddStatus {
    data class Success(val playlistName: String) : AddStatus
    data class AlreadyAdded(val playlistName: String) : AddStatus
}