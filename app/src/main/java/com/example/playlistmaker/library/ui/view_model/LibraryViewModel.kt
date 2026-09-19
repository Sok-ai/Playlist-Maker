package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.library.domain.api.MusicPlayer
import com.example.playlistmaker.library.domain.model.PlayerUiState
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.core.domain.model.Song
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val musicPlayer: MusicPlayer,
    private val searchInteractor: SearchInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val songId: Long
) : ViewModel() {
    private var timerJob: Job? = null

    private val _uiState = MutableLiveData(PlayerUiState())
    fun observeUiState(): LiveData<PlayerUiState> = _uiState

    init {
        checkIsFavorite(songId)
        gettingMusic()
    }

    private fun gettingMusic() {
        viewModelScope.launch {
            val songFromDB = favoriteInteractor.getFavoriteById(songId)

            if (songFromDB != null) {
                setupInitPlayer(songFromDB)
                return@launch
            }

            val resSong = searchInteractor.getSongByIdFromHistory(songId)

            if (resSong != null) {
                isLastSong(resSong)
                return@launch
            }

            loadLastTrack()
        }
    }

    private fun loadLastTrack() {
        val songData = searchInteractor.getLastTrackFromHistory()
        if (songData != null) {
            setupInitPlayer(songData)
        } else {
            _uiState.value = _uiState.value?.copy(
                isLoading = false,
                isReady = false,
                song = null
            )
        }
    }

    private fun isLastSong(newSong: Song) {
        val lastTrack = searchInteractor.getLastTrackFromHistory()

        if (lastTrack?.trackId != newSong.trackId) {
            searchInteractor.saveLastTrack(newSong)
        }

        setupInitPlayer(newSong)
    }

    private fun setupInitPlayer(resSong: Song) {
        _uiState.value = _uiState.value?.copy(isLoading = true)

        musicPlayer.preparePlayer(
            resSong.previewUrl
        ) {
            _uiState.value =
                _uiState.value?.copy(
                    song = resSong,
                    isReady = true,
                    isLoading = false
                )
        }

        musicPlayer.setOnCompletionListener {
            _uiState.value =
                _uiState.value?.copy(
                    isPlaying = false,
                    currentPosition = 0,
                )
            musicPlayer.resetToStart()
            stopUpdatingTime()
        }
    }

    private fun checkIsFavorite(idSong: Long) {
        viewModelScope.launch {
            favoriteInteractor.isFavorite(idSong).collect {
                _uiState.value = _uiState.value?.copy(isFavorite = it)
            }
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
            when {
                musicPlayer.isPlayer() -> {
                    _uiState.value = _uiState.value?.copy(isPlaying = false)
                    musicPlayer.pausePlayer()
                    stopUpdatingTime()
                }

                musicPlayer.isPreparedOrPause() -> {
                    _uiState.value = _uiState.value?.copy(isPlaying = true)
                    musicPlayer.startPlayer()
                    startUpdatingTime()
                }
            }
        }
    }

    private fun startUpdatingTime() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (musicPlayer.isPlayer()) {
                ensureActive()
                delay(DELAY_UPDATE_TIMER)
                _uiState.value =
                    _uiState.value?.copy(currentPosition = musicPlayer.currentPosition())
            }
        }
    }

    private fun stopUpdatingTime() {
        timerJob?.cancel()
        timerJob = null
        _uiState.value = _uiState.value?.copy(
            currentPosition = musicPlayer.currentPosition()
        )
    }

    private fun releasePlayer() {
        musicPlayer.release()
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