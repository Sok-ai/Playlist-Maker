package com.example.playlistmaker.search.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository
import kotlin.concurrent.thread

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val historyRepository: SearchHistoryRepository
) : SearchInteractor {
    override fun searchSongs(
        expression: String,
        consumer: SearchInteractor.SearchConsumer
    ) {
        thread(start = true) {
            val result = searchRepository.searchSongs(expression)
            Handler(Looper.getMainLooper()).post {
                consumer.consume(result)
            }
        }
    }

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