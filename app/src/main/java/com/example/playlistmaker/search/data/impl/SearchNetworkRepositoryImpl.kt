package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.SongSearchRequest
import com.example.playlistmaker.search.data.dto.SongsSearchResponse
import com.example.playlistmaker.search.data.mapper.MapperNetwork
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.api.SearchRepository

class SearchNetworkRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: MapperNetwork
) : SearchRepository {
    override fun searchSongs(expression: String): SearchResult {
        val response = networkClient.doRequest(SongSearchRequest(expression))
        return when (response.resultCode) {
            200 -> {
                val songs = (response as SongsSearchResponse).results.map {
                    mapper.toSong(it)
                }
                if (songs.isEmpty()) {
                    SearchResult.Empty
                } else {
                    SearchResult.Success(songs)
                }
            }

            else -> {
                SearchResult.Error
            }
        }
    }
}