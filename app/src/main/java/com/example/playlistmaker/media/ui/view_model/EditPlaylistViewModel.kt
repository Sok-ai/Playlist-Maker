package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.media.domain.api.FileInteractor
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    playlistInteractor: PlaylistInteractor,
    fileInteractor: FileInteractor
) : CreatePlaylistViewModel(playlistInteractor, fileInteractor) {

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(
                playlistId
            ) ?: return@launch
            _playlistState.value = _playlistState.value?.copy(
                id = playlist.id,
                coverUri = null,
                existingCoverPath = playlist.pathImage,
                name = playlist.name,
                description = playlist.description,
                songCount = playlist.songCount
            )
        }
    }

    override suspend fun save(playlist: Playlist) {
        playlistInteractor.updatePlaylist(playlist)
        _responseSavePlaylist.value = playlist.name
    }
}