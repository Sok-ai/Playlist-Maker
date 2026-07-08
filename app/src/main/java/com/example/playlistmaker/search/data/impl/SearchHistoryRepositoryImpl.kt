package com.example.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.StorageClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Song
import com.google.gson.Gson

const val SONG_LAST_TRACK_KEY = "song_last_track_key"

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<List<Song>>,
    private val sharedPref: SharedPreferences
) :
    SearchHistoryRepository {

    override fun getHistory(): List<Song> {
        return storage.getData() ?: emptyList()
    }

    override fun addToHistory(song: Song) {
        val currentList = storage.getData()?.toMutableList() ?: mutableListOf()

        if (currentList.isEmpty()) currentList.add(song)

        val index: Int = currentList.indexOfFirst { it.trackId == song.trackId }
        if (index != -1) {
            val existingSong = currentList.removeAt(index)
            currentList.add(0, existingSong)
        } else {
            if (currentList.size == 10) {
                currentList.removeAt(currentList.lastIndex)
            }
            currentList.add(0, song)
        }

        storage.storeData(currentList)
    }

    override fun clearHistory() {
        storage.clearData()
    }

    override fun getSongById(songIdParam: Long): Song? {
        val currentList = storage.getData()
        return currentList?.firstOrNull {
            it.trackId == songIdParam
        }
    }

    override fun saveLastTrack(song: Song) {
        val json = Gson().toJson(song)
        sharedPref.edit().putString(SONG_LAST_TRACK_KEY, json).apply()
    }

    override fun getLastTrack(): Song? {
        val json = sharedPref.getString(SONG_LAST_TRACK_KEY, null)
        return if (json.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(json, Song::class.java)
        }
    }

}