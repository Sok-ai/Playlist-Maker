package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.media.ui.model.ShareData
import com.example.playlistmaker.utils.SingleLiveEvent
import com.example.playlistmaker.utils.TimeFormatter
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _closeEvent = SingleLiveEvent<Unit>()
    fun observeCloseEvent(): LiveData<Unit> = _closeEvent
    private val _playlist = MutableLiveData<Playlist?>()
    fun observePlaylist(): LiveData<Playlist?> = _playlist

    private val _songsToPlaylist = MutableLiveData<List<Song>>()
    fun observeSongsToPlaylist(): LiveData<List<Song>> = _songsToPlaylist

    init {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId)
            if (playlist == null) {
                _closeEvent.value = Unit
                return@launch
            }
            _playlist.value = playlist
        }
        viewModelScope.launch {
            playlistInteractor.getTracksForPlaylist(playlistId).collect { songs ->
                _songsToPlaylist.value = songs
            }
        }
    }

    fun getShareData(): ShareData? {
        val playlist = _playlist.value ?: return null
        val songs = _songsToPlaylist.value.orEmpty()
        val tracksList = songs.mapIndexed { index, song ->
            "${index + 1}. ${song.artistName} - ${song.trackName} " +
                    "(${TimeFormatter.format(song.trackTimeMillis)})"
        }.joinToString("\n")

        return ShareData(
            name = playlist.name,
            description = playlist.description,
            tracksCount = songs.size,
            tracksList = tracksList
        )
    }

    fun deleteSong(songId: Long) {
        viewModelScope.launch {
            playlistInteractor.deleteSong(playlistId, songId)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistId)
            _closeEvent.value = Unit
        }
    }
}