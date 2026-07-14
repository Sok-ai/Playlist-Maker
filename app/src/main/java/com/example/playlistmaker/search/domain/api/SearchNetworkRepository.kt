package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult

interface SearchNetworkRepository {
    fun searchSongs(expression: String): SearchResult

}