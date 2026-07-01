package com.example.playlistmaker.search.ui

import com.example.playlistmaker.domain.model.Song

fun interface OnSongActionListener {
    fun onSongClick(song: Song)
}