package com.example.playlistmaker.utils

import android.os.Handler
import android.os.Looper

class PeriodicAction(private val intervalMillis: Long) {
    private var job: Runnable? = null
    private val handler: Handler = Handler(Looper.getMainLooper())

    fun start(action: () -> Unit) {
        stop()

        val runnable = object : Runnable {
            override fun run() {
                action()
                handler.postDelayed(this, intervalMillis)
            }
        }
        job = runnable
        handler.post(runnable)
    }

    fun stop() {
        job?.let {
            handler.removeCallbacks(it)
        }
        job = null
    }
}