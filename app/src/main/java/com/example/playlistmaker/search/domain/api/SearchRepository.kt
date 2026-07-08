package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult

interface SearchRepository {
    fun searchSongs(expression: String): SearchResult

}