package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.StorageClient
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefStorageClient<T>
    (
    private val sharedPref: SharedPreferences,
    private val dataName: String,
    private val type: Type
) :
    StorageClient<T> {

    override fun storeData(data: T) {
        sharedPref.edit().putString(dataName, Gson().toJson(data)).apply()
    }

    override fun getData(): T {
        val json = sharedPref.getString(dataName, null)
        return Gson().fromJson(json, type)
    }

    override fun clearData() {
        sharedPref.edit().remove(dataName).apply()
    }
}