package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.core.domain.model.Song


interface SearchHistoryRepository {
    fun getHistory(): List<Song>
    fun addToHistory(song: Song)
    fun clearHistory()
    fun getSongById(songIdParam: Long): Song?
    fun saveLastTrack(song: Song)
    fun getLastTrack(): Song?
}