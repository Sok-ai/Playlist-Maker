package com.example.playlistmaker.media.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePlaylistViewModel : ViewModel() {
    private val _playlistState = MutableLiveData(CreatePlaylistState())
    fun observePlaylistState(): LiveData<CreatePlaylistState> = _playlistState

    fun onNameChange(text: String) {
        _playlistState.value = _playlistState.value?.copy(name = text)
    }

    fun getPlaylistName() = _playlistState.value?.name.orEmpty()

    fun onDescriptionChange(text: String) {
        _playlistState.value = _playlistState.value?.copy(description = text)
    }

    fun onImageChange(coverUri: Uri) {
        _playlistState.value = _playlistState.value?.copy(coverUri = coverUri)
    }

    fun hasUnsavedData(): Boolean {
        return _playlistState.value?.hasUnsavedData ?: false
    }
}