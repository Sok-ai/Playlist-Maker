package com.example.playlistmaker.media.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.api.FileInteractor
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.utils.SingleLiveEvent
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val fileInteractor: FileInteractor
) : ViewModel() {

    private val debouncedCreatePlaylist = debounce<Unit>(
        300L,
        viewModelScope,
        false,
        action = {
            createPlaylistInternal()
        })
    private val _playlistState = MutableLiveData(CreatePlaylistState())
    fun observePlaylistState(): LiveData<CreatePlaylistState> = _playlistState

    private val _responseSavePlaylist = SingleLiveEvent<String>()
    fun observeResponseSavePlaylist(): LiveData<String> = _responseSavePlaylist

    fun createPlaylist() {
        debouncedCreatePlaylist(Unit)
    }

    private fun createPlaylistInternal() {
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
                Log.e("PlaylistMaker", "create failed", e)
            }
        }
    }

    private suspend fun saveCoverFile(uri: Uri?): String? = uri?.let { uri ->
        fileInteractor.saveFile(uri.toString())
    }

    fun onNameChange(text: String) {
        val current = _playlistState.value ?: return
        if (current.name == text) return
        _playlistState.value = current.copy(name = text)
    }

    fun onDescriptionChange(text: String) {
        val current = _playlistState.value ?: return
        if (current.description == text) return
        _playlistState.value = current.copy(description = text)
    }

    fun onImageChange(coverUri: Uri) {
        val current = _playlistState.value ?: return
        if (current.coverUri == coverUri) return
        _playlistState.value = current.copy(coverUri = coverUri)
    }

    fun hasUnsavedData(): Boolean {
        return _playlistState.value?.hasUnsavedData ?: false
    }
}