package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeFormatter {
    fun format(trackTimeMillis: Long): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    fun formatMinutes(durationSum: Long): Int =
        TimeUnit.MILLISECONDS.toMinutes(durationSum).toInt()
}