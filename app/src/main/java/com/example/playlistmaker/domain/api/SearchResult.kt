package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Song

sealed class SearchResult {
    data class Success(val songs: List<Song>) : SearchResult()
    object Empty : SearchResult()
    data class Error(val message: String) : SearchResult()
}