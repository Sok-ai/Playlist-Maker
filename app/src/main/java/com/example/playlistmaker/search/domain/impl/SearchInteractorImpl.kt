package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchNetworkRepository
import com.example.playlistmaker.search.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val searchRepository: SearchNetworkRepository,
    private val historyRepository: SearchHistoryRepository
) : SearchInteractor {
    override fun searchSongs(expression: String): Flow<SearchResult> =
        searchRepository.searchSongs(expression)


    override fun getHistory(): List<Song> {
        return historyRepository.getHistory()
    }

    override fun getSongById(id: Long): Song? {
        return historyRepository.getSongById(id)
    }

    override fun addToHistory(song: Song) {
        return historyRepository.addToHistory(song)
    }

    override fun clearHistory() {
        return historyRepository.clearHistory()
    }

    override fun saveLastTrack(song: Song) {
        historyRepository.saveLastTrack(song)
    }

    override fun getLastTrack(): Song? {
        return historyRepository.getLastTrack()
    }
}