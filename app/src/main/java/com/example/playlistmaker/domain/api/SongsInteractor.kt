package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Song

interface SongsInteractor {
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