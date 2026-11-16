package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.core.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.library.domain.model.PlayerUiState
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.library.domain.api.MusicPlayer
import com.example.playlistmaker.library.services.PlayerState
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val song: Song
) : ViewModel() {

    private var musicPlayer: MusicPlayer? = null

    private var playerState: PlayerState = PlayerState.Default

    private val _uiState = MutableLiveData(PlayerUiState())
    fun observeUiState(): LiveData<PlayerUiState> = _uiState

    private val _playlists = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = _playlists

    private val _addStatus = SingleLiveEvent<AddStatus>()
    fun observeAddStatus(): LiveData<AddStatus> = _addStatus

    private var playerStateJob: Job? = null

    init {
        checkIsFavorite(song.trackId)
        getPlaylists()
    }

    fun setMusicPlayer(musicPlayer: MusicPlayer) {
        this.musicPlayer = musicPlayer
        collectPlayerState()
        setupInitPlayer(song)
    }

    fun removeMusicPlayer() {
        this.musicPlayer = null
        playerStateJob?.cancel()
    }

    private fun collectPlayerState() {
        playerStateJob?.cancel()
        playerStateJob = viewModelScope.launch {
            musicPlayer?.playerState?.collect { state ->
                playerState = state
                when (state) {
                    PlayerState.Default -> {}
                    is PlayerState.Paused -> {
                        _uiState.value = _uiState.value?.copy(
                            isPlaying = false,
                            currentPosition = state.position
                        )
                    }

                    is PlayerState.Playing -> {
                        _uiState.value = _uiState.value?.copy(
                            isPlaying = true,
                            currentPosition = state.position
                        )
                    }

                    PlayerState.Prepared -> {
                        _uiState.value = _uiState.value?.copy(
                            isPlaying = false,
                            currentPosition = 0,
                            isReady = true
                        )
                    }
                }
            }
        }
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { list ->
                _playlists.value = list
            }
        }
    }

    private fun setupInitPlayer(resSong: Song) {
        _uiState.value = _uiState.value?.copy(isLoading = true)

        musicPlayer?.preparePlayer()
        musicPlayer?.setOnPrepareListener {
            _uiState.value =
                _uiState.value?.copy(
                    song = resSong,
                    isReady = true,
                    isLoading = false
                )
        }
    }

    private fun checkIsFavorite(idSong: Long) {
        viewModelScope.launch {
            favoriteInteractor.isFavorite(idSong).collect {
                _uiState.value = _uiState.value?.copy(isFavorite = it)
            }
        }
    }

    fun addSongToPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            if (playlistInteractor.isSongInPlaylist(playlist.id, song.trackId)) {
                _addStatus.value = AddStatus.AlreadyAdded(playlist.name)
                return@launch
            }
            playlistInteractor.addTrackToPlaylist(playlist = playlist, song = song)
            _addStatus.value = AddStatus.Success(playlist.name)
        }
    }

    fun onClickFavorite() {
        val song = _uiState.value?.song ?: return
        viewModelScope.launch {
            if (_uiState.value?.isFavorite == true) {
                favoriteInteractor.deleteFavorites(song.trackId)
            } else {
                favoriteInteractor.insertFavorite(song)
            }
        }
    }

    fun onClickPlayer() {
        if (_uiState.value?.isReady == true) {
            when (playerState) {
                is PlayerState.Playing -> {
                    musicPlayer?.pausePlayer()
                }

                is PlayerState.Prepared, is PlayerState.Paused -> {
                    musicPlayer?.startPlayer()
                    musicPlayer?.startUpdatingTime()
                }

                else -> {}
            }
        }
    }

    fun onForegroundStarted() {
        if (playerState is PlayerState.Playing) {
            musicPlayer?.startForegroundMode()
        }
    }

    fun onForegroundStopped() {
        musicPlayer?.stopForegroundMode()
    }

    override fun onCleared() {
        timerJob?.cancel()
        releasePlayer()
    }

    fun onPause() {
        if (musicPlayer.isPlayer()) {
            stopUpdatingTime()
            musicPlayer.pausePlayer()
            _uiState.value = _uiState.value?.copy(isPlaying = false)
        }
    }

    companion object {
        const val DELAY_UPDATE_TIMER = 300L
    }
}