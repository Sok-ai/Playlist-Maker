package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class SongsResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<Song>
)

data class Song(
    @SerializedName("trackId") val trackId: Long,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("collectionName") val collectionName: String = "",
    @SerializedName("releaseDate") val releaseDate: String = "",
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("trackTimeMillis") private val trackTimeMillis: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String
) {
    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    val coverImagePlayer: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    val yearReleaseTrack: String
        get() = releaseDate.substring(0, 4)
}