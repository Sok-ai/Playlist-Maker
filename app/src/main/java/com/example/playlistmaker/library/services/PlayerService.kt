package com.example.playlistmaker.library.services

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.library.domain.api.MusicPlayer
import com.example.playlistmaker.library.services.notification.PlayerNotificationHelper
import com.example.playlistmaker.library.ui.URL_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerService : Service(), MusicPlayer {
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default)
    override val playerState = _playerState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var onPrepareCallback: (() -> Unit)? = null

    private var timerJob: Job? = null
    private val playerServiceBinder = PlayerServiceBinder()

    private val helperNotification = PlayerNotificationHelper(this)

    private var song: Song? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        helperNotification.createChannel()
    }

    override fun onBind(intent: Intent?): IBinder {
        song = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(URL_KEY, Song::class.java)
        } else {
            intent?.getParcelableExtra(URL_KEY)
        }

        return playerServiceBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopUpdatingTime()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
        stopUpdatingTime()
        stopForegroundMode()
        onPrepareCallback = null
        mediaPlayer = null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun preparePlayer() {
        if (song == null) return

        if (mediaPlayer == null) mediaPlayer = MediaPlayer()

        mediaPlayer?.setDataSource(song!!.previewUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setVolume(0.6f, 0.6f)
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared
            onPrepareCallback?.invoke()
        }
        mediaPlayer?.setOnCompletionListener {
            resetToStart()
        }
    }

    override fun startUpdatingTime() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                ensureActive()
                delay(DELAY_UPDATE_TIMER)
                val currentPos = mediaPlayer?.currentPosition ?: 0
                _playerState.value = PlayerState.Playing(currentPos)
            }
        }
    }

    override fun stopUpdatingTime() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(currentPosition())
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        stopUpdatingTime()
        _playerState.value = PlayerState.Paused(currentPosition())
    }


    override fun currentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    override fun release() {
        mediaPlayer?.release()
        timerJob?.cancel()
        _playerState.value = PlayerState.Default
    }

    override fun setOnPrepareListener(listener: () -> Unit) {
        onPrepareCallback = listener
    }

    override fun resetToStart() {
        mediaPlayer?.seekTo(0)
        stopUpdatingTime()
        stopForegroundMode()
        _playerState.value = PlayerState.Prepared
    }

    override fun startForegroundMode() {
        song?.let {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                helperNotification.buildNotification(it),
                getForegroundServiceTypeConstant()
            )
        }
    }

    override fun stopForegroundMode() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun getForegroundServiceTypeConstant(): Int =
        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    private companion object {
        const val DELAY_UPDATE_TIMER = 300L
        const val SERVICE_NOTIFICATION_ID = 1
    }
}