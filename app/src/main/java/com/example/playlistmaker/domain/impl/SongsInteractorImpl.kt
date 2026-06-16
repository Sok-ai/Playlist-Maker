package com.example.playlistmaker.domain.impl

import android.os.Looper
import android.os.Handler
import com.example.playlistmaker.domain.api.SongsInteractor
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.model.Song
import kotlin.concurrent.thread

class SongsInteractorImpl(private val repository: SongsRepository) : SongsInteractor {
    override fun searchSongs(
        expression: String,
        consumer: SongsInteractor.SearchConsumer
    ) {
        thread(start = true) {
            val result = repository.searchSongs(expression)
            Handler(Looper.getMainLooper()).post {
                consumer.consume(result)
            }
        }
    }

    override fun getHistory(): List<Song> {
        return repository.getHistory()
    }

    override fun getSongById(id: Long): Song? {
        return repository.getSongById(id)
    }

    override fun addToHistory(song: Song) {
        return repository.addToHistory(song)
    }

    override fun clearHistory() {
        return repository.clearHistory()
    }

    override fun saveLastTrack(song: Song) {
        repository.saveLastTrack(song)
    }

    override fun getLastTrack(): Song? {
        return repository.getLastTrack()
    }
}