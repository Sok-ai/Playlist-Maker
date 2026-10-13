package com.example.playlistmaker.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String
) : Parcelable