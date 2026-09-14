package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object TimeFormatter {
    fun format(trackTimeMillis: Long): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}