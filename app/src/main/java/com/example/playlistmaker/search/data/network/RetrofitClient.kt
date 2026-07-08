package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.api.SongApi
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SongSearchRequest

class RetrofitClient(private val context: Context, private val networkService: SongApi) :
    NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply {
                resultCode = -1
            }
        }
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

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            }
        }
        return false
    }
}