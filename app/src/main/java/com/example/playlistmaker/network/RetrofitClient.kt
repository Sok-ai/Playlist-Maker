package com.example.playlistmaker.network

import com.example.playlistmaker.SongApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    val trackService: SongApi = retrofit.create(SongApi::class.java)
}