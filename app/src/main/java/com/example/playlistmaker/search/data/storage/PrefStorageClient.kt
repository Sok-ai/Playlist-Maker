package com.example.playlistmaker.search.data.storage

import android.content.Context
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.search.data.StorageClient
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefStorageClient<T>
    (context: Context, private val dataName: String, private val type: Type) :
    StorageClient<T> {
    private val pref =
        context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)


    override fun storeData(data: T) {
        pref.edit().putString(dataName, Gson().toJson(data)).apply()
    }

    override fun getData(): T {
        val json = pref.getString(dataName, null)
        return Gson().fromJson(json, type)
    }

    override fun clearData() {
        pref.edit().remove(dataName).apply()
    }
}