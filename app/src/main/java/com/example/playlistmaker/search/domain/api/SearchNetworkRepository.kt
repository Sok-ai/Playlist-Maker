package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchNetworkRepository {
    fun searchSongs(expression: String): Flow<SearchResult>

}