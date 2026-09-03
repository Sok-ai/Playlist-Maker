package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchSongs(expression: String): Flow<SearchResult>
    fun getHistory(): List<Song>
    fun getSongById(id: Long): Song?
    fun addToHistory(song: Song)
    fun clearHistory()
    fun saveLastTrack(song: Song)
    fun getLastTrack(): Song?
}