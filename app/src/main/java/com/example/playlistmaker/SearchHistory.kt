package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SONG_SEARCH_HISTORY_KEY = "song_history_key"

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
}