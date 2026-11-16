package com.example.playlistmaker.library.services

sealed class PlayerState {
    object Default : PlayerState()
    object Prepared : PlayerState()
    data class Playing(val position: Int) : PlayerState()
    data class Paused(val position: Int) : PlayerState()
}