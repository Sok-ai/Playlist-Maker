package com.example.playlistmaker.search.domain.model

import com.example.playlistmaker.core.domain.model.Song

sealed interface SearchResult {
    object Loading : SearchResult
    data class Success(val songs: List<Song>) : SearchResult
    object Empty : SearchResult
    object Error : SearchResult
}