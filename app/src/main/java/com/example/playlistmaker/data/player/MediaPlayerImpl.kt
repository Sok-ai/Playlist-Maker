package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MusicPlayer
import com.example.playlistmaker.domain.state.PlayerState.STATE_DEFAULT
import com.example.playlistmaker.domain.state.PlayerState.STATE_PAUSED
import com.example.playlistmaker.domain.state.PlayerState.STATE_PLAYING
import com.example.playlistmaker.domain.state.PlayerState.STATE_PREPARED

class MediaPlayerImpl : MusicPlayer {
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var onCompletionCallback: (() -> Unit)? = null

    override fun preparePlayer(previewUrl: String, onPrepare: () -> Unit) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setVolume(0.6f, 0.6f)
        mediaPlayer.setOnPreparedListener {
            onPrepare()
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            onCompletionCallback?.invoke()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun isPlayer(): Boolean = playerState == STATE_PLAYING
    override fun isPreparedOrPause(): Boolean =
        playerState == STATE_PREPARED || playerState == STATE_PAUSED

    override fun currentPosition(): Int = mediaPlayer.currentPosition

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionCallback = listener
    }
}