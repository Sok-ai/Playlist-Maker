package com.example.playlistmaker.core.ui

import com.example.playlistmaker.core.domain.model.Playlist

fun interface OnPlaylistActionListener {
    fun onPlaylistClick(playlist: Playlist)
}