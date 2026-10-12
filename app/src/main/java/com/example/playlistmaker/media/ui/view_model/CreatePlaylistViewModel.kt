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

open class CreatePlaylistViewModel(
    protected val playlistInteractor: PlaylistInteractor,
    protected val fileInteractor: FileInteractor
) : ViewModel() {

    private val debouncedCreatePlaylist = debounce<Unit>(
        300L,
        viewModelScope,
        false,
        action = {
            createPlaylistInternal()
        })

    protected val _playlistState = MutableLiveData(CreatePlaylistState())
    fun observePlaylistState(): LiveData<CreatePlaylistState> = _playlistState

    protected val _responseSavePlaylist = SingleLiveEvent<String>()
    fun observeResponseSavePlaylist(): LiveData<String> = _responseSavePlaylist

    fun createPlaylist() {
        debouncedCreatePlaylist(Unit)
    }

    open suspend fun save(playlist: Playlist) {
        playlistInteractor.insertPlaylist(playlist)
        _responseSavePlaylist.value = playlist.name
    }

    private fun createPlaylistInternal() {
        val state = _playlistState.value ?: return
        viewModelScope.launch {
            try {
                val coverPath = if (state.coverUri != null) {
                    saveCoverFile(state.coverUri)
                } else {
                    state.existingCoverPath
                }
                val playlist = Playlist(
                    id = state.id,
                    name = state.name,
                    description = state.description,
                    pathImage = coverPath,
                    songCount = state.songCount
                )
                save(playlist)
            } catch (e: Exception) {
                Log.e("PlaylistMaker", "create failed", e)
            }
        }
    }

    protected suspend fun saveCoverFile(uri: Uri?): String? = uri?.let { uri ->
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