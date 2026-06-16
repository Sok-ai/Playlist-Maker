package com.example.playlistmaker.data.history

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Song
import com.google.gson.Gson

const val SONG_SEARCH_HISTORY_KEY = "song_history_key"
const val SONG_LAST_TRACK_KEY = "song_last_track_key"

class SearchHistory(val sharedPreferences: SharedPreferences) {
    fun getSongs(): List<Song> {
        val json = sharedPreferences.getString(SONG_SEARCH_HISTORY_KEY, null)
        return convertJsonToSong(json ?: "").toList()
    }

    fun putSongs(song: Song) {
        val currentList = getSongs().toMutableList()

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
        val json = convertSongToJson(currentList)
        sharedPreferences.edit().putString(SONG_SEARCH_HISTORY_KEY, json).apply()
    }

    fun clearSongsFromShared() {
        sharedPreferences.edit().remove(SONG_SEARCH_HISTORY_KEY).apply()
    }

    private fun convertJsonToSong(json: String): Array<Song> {
        return if (json.isEmpty()) {
            emptyArray()
        } else {
            Gson().fromJson(json, Array<Song>::class.java)
        }
    }

    private fun convertSongToJson(song: List<Song>): String {
        return Gson().toJson(song)
    }

    fun getSongById(songIdParam: Long): Song? {
        val currentList = getSongs().asSequence()
        return currentList.firstOrNull {
            it.trackId == songIdParam
        }
    }

    fun saveLastTrack(song: Song) {
        val json = Gson().toJson(song)
        sharedPreferences.edit().putString(SONG_LAST_TRACK_KEY, json).apply()
    }

    fun getLastTrack(): Song? {
        val json = sharedPreferences.getString(SONG_LAST_TRACK_KEY, null)
        return if (json.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(json, Song::class.java)
        }
    }
}