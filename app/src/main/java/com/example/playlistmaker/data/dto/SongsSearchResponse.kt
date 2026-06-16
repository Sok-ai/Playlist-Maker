package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class SongsSearchResponse(
    @SerializedName("results") val results: List<SongDto>
) : Response()
