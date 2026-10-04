package com.example.playlistmaker.core.domain.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val pathImage: String?,
    val songs: List<Long>,
    val songCount: Int
)