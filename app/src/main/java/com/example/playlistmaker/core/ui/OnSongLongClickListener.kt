package com.example.playlistmaker.core.ui

import com.example.playlistmaker.core.domain.model.Song

fun interface OnSongLongClickListener {
    fun onSongLongClick(song: Song)
}