package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.model.Song

interface SearchInteractor {
    fun searchSongs(expression: String, consumer: SearchConsumer)
    fun getHistory(): List<Song>
    fun getSongById(id: Long): Song?
    fun addToHistory(song: Song)
    fun clearHistory()
    fun saveLastTrack(song: Song)
    fun getLastTrack(): Song?

    fun interface SearchConsumer {
        fun consume(foundSong: SearchResult)
    }
}