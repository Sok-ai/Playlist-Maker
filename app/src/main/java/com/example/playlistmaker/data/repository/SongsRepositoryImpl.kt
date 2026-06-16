package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.SongSearchRequest
import com.example.playlistmaker.data.dto.SongsSearchResponse
import com.example.playlistmaker.data.history.SearchHistory
import com.example.playlistmaker.data.mapper.MapperNetwork
import com.example.playlistmaker.domain.api.SearchResult
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.model.Song

class SongsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistory: SearchHistory,
    private val mapper: MapperNetwork
) : SongsRepository {
    override fun searchSongs(expression: String): SearchResult {
        val response = networkClient.doRequest(SongSearchRequest(expression))
        return if (response.resultCode == 200) {
            val songs = (response as SongsSearchResponse).results.map {
                mapper.toSong(it)
            }
            if (songs.isEmpty()) {
                SearchResult.Error("Ничего не найдено")
            } else {
                SearchResult.Success(songs)
            }
        } else {
            SearchResult.Error("Ошибка сети")
        }
    }

    override fun getHistory(): List<Song> {
        return searchHistory.getSongs()
    }

    override fun getSongById(id: Long): Song? {
        return searchHistory.getSongById(id)
    }

    override fun addToHistory(song: Song) {
        searchHistory.putSongs(song)
    }

    override fun clearHistory() {
        searchHistory.clearSongsFromShared()
    }

    override fun saveLastTrack(song: Song) {
        searchHistory.saveLastTrack(song)
    }

    override fun getLastTrack(): Song? {
        return searchHistory.getLastTrack()
    }
}