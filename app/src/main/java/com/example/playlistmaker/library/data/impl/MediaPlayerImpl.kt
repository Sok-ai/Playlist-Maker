package com.example.playlistmaker.library.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.library.domain.api.MusicPlayer
import com.example.playlistmaker.library.domain.model.PlayerState

class MediaPlayerImpl(private val mediaPlayer: MediaPlayer) : MusicPlayer {
    private var playerState = PlayerState.STATE_DEFAULT
    private var onCompletionCallback: (() -> Unit)? = null

    override fun preparePlayer(previewUrl: String, onPrepare: () -> Unit) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setVolume(0.6f, 0.6f)
        mediaPlayer.setOnPreparedListener {
            onPrepare()
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            onCompletionCallback?.invoke()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun isPlayer(): Boolean = playerState == PlayerState.STATE_PLAYING
    override fun isPreparedOrPause(): Boolean =
        playerState == PlayerState.STATE_PREPARED || playerState == PlayerState.STATE_PAUSED

    override fun currentPosition(): Int = mediaPlayer.currentPosition

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionCallback = listener
    }
}