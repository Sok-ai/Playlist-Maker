package com.example.playlistmaker.utils

import android.os.Handler
import android.os.Looper

class Debounce(private val delayMillis: Long) {
    private var job: Runnable? = null
    private var handler = Handler(Looper.getMainLooper())

    fun run(action: () -> Unit) {
        job?.let { handler.removeCallbacks(it) }
        job = Runnable { action() }
        handler.postDelayed(job!!, delayMillis)
    }

    fun cancel() {
        job?.let { handler.removeCallbacks(it) }
    }
}