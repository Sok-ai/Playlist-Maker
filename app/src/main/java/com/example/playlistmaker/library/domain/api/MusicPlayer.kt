package com.example.playlistmaker.library.domain.api

interface MusicPlayer {
    fun preparePlayer(previewUrl: String, onPrepare: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun isPreparedOrPause(): Boolean
    fun isPlayer(): Boolean
    fun currentPosition(): Int
    fun release()
    fun setOnCompletionListener(listener: () -> Unit)
    fun resetToStart()
}