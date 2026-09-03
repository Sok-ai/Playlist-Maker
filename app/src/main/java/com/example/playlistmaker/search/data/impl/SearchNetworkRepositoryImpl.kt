package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.SongSearchRequest
import com.example.playlistmaker.search.data.dto.SongsSearchResponse
import com.example.playlistmaker.search.data.mapper.MapperNetwork
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.api.SearchNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNetworkRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: MapperNetwork
) : SearchNetworkRepository {
    override fun searchSongs(expression: String): Flow<SearchResult> = flow {
        val response = networkClient.doRequest(SongSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val songs = (response as SongsSearchResponse).results.map {
                    mapper.toSong(it)
                }
                if (songs.isEmpty()) {
                    emit(SearchResult.Empty)
                } else {
                    emit(SearchResult.Success(songs))
                }
            }

            else -> {
                emit(SearchResult.Error)
            }
        }
    }
}