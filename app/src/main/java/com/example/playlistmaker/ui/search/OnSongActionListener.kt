package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.model.Song

fun interface OnSongActionListener {
    fun onSongClick(song: Song)
}