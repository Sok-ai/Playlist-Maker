package com.example.playlistmaker.media.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.api.FileInteractor
import com.example.playlistmaker.media.domain.api.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val fileInteractor: FileInteractor
) : ViewModel() {
    private val _playlistState = MutableLiveData(CreatePlaylistState())
    fun observePlaylistState(): LiveData<CreatePlaylistState> = _playlistState

    private val _responseSavePlaylist = SingleLiveEvent<String>()
    fun observeResponseSavePlaylist(): LiveData<String> = _responseSavePlaylist

    fun createPlaylist() {
        val state = _playlistState.value ?: return
        viewModelScope.launch {
            try {
                val coverPath = saveCoverFile(state.coverUri)
                val playlist = Playlist(
                    name = state.name,
                    description = state.description,
                    pathImage = coverPath,
                    songs = emptyList(),
                    songCount = 0
                )
                playlistInteractor.insertPlaylist(playlist)
                _responseSavePlaylist.value = state.name
            } catch (e: Exception) {
                Log.e("Playlist_maker", e.message ?: "Unknown error", e)
            }
        }
    }

    private suspend fun saveCoverFile(uri: Uri?): String? = uri?.let { uri ->
        fileInteractor.saveFile(uri.toString())
    }

    fun onNameChange(text: String) {
        _playlistState.value = _playlistState.value?.copy(name = text)
    }

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