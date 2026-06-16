package com.example.playlistmaker.data.network.api

import com.example.playlistmaker.data.dto.SongsSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("/search?entity=song")
    fun searchSongs(@Query("term") requestSearch: String): Call<SongsSearchResponse>
}