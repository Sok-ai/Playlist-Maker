package com.example.playlistmaker

import android.view.View

fun interface OnSongActionListener {
    fun onSongClick(song: Song)
}