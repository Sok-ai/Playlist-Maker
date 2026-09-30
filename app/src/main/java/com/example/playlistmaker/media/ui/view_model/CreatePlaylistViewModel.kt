package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePlaylistViewModel : ViewModel() {
    private val _isCreateEnabled = MutableLiveData(false)
    fun observeIsCreateEnabled(): LiveData<Boolean> = _isCreateEnabled

    fun onNameChange(text: String) {
        _isCreateEnabled.value = text.isNotEmpty()
    }
}