package com.example.playlistmaker.search.ui

import com.example.playlistmaker.core.domain.model.Song

fun interface OnSongActionListener {
    fun onSongClick(song: Song)
}