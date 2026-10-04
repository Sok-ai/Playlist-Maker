package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.core.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = _playlists

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { list ->
                _playlists.value = list
            }
        }
    }
}