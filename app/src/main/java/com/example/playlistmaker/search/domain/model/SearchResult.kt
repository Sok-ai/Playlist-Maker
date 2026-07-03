package com.example.playlistmaker.search.domain.model

sealed class SearchResult {
    object Loading : SearchResult()
    data class Success(val songs: List<Song>) : SearchResult()
    object Empty : SearchResult()
    object Error : SearchResult()
}