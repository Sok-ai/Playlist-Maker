package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SongSearchRequest
import com.example.playlistmaker.data.network.api.SongApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient : NetworkClient {
    private val retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val songService: SongApi = retrofit.create(SongApi::class.java)

    override fun doRequest(dto: Any): Response {
        when (dto) {
            is SongSearchRequest -> {
                val resp = songService.searchSongs(dto.expression).execute()
                val body = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }
            }

            else -> {
                return Response().apply { resultCode = 400 }
            }
        }
    }
}