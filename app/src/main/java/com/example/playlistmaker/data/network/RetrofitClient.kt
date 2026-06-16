package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SongSearchRequest
import com.example.playlistmaker.data.network.api.SongApi

class RetrofitClient(private val networkService: SongApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        when (dto) {
            is SongSearchRequest -> {
                val resp = networkService.searchSongs(dto.expression).execute()
                val body = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }
            }

            else -> {
                return Response().apply { resultCode = 400 }
            }
        }
    }
}