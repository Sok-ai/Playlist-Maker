package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.search.data.dto.SongsSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("search?entity=song")
    fun searchSongs(@Query("term") requestSearch: String): Call<SongsSearchResponse>
}