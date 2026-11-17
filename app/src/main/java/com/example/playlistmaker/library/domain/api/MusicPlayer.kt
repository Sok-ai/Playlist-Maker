package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.library.services.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface MusicPlayer {
    val playerState: StateFlow<PlayerState>
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun currentPosition(): Int
    fun release()
    fun resetToStart()
    fun setOnPrepareListener(listener: () -> Unit)
    fun stopUpdatingTime()
    fun startUpdatingTime()
    fun stopForegroundMode()
    fun startForegroundMode()
}