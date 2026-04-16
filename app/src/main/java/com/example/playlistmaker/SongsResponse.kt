package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class SongsResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<Song>
)

data class Song(
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") private val trackTimeMillis: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String
) {
    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}