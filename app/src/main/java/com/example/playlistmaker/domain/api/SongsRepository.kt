package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Song

interface SongsRepository {
    fun searchSongs(expression: String): SearchResult
    fun getHistory(): List<Song>
    fun getSongById(id: Long): Song?
    fun addToHistory(song: Song)
    fun clearHistory()
    fun saveLastTrack(song: Song)
    fun getLastTrack(): Song?

}