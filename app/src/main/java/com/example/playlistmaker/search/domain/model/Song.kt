package com.example.playlistmaker.search.domain.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Song(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String,
    val country: String,
    private val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String
) {
    val coverImagePlayer: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    val yearReleaseTrack: String
        get() = releaseDate.substring(0, 4)

    companion object {
        fun formatDuration(time: Int): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
        }
    }
}