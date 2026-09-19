package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {
    private var clickDebounceJob: Job? = null

    private val _favoriteList = MutableLiveData<List<Song>>()
    fun observeFavoriteList(): LiveData<List<Song>> = _favoriteList

    private val _navigateToLibrary = SingleLiveEvent<Long>()
    fun observeNavigateToLibrary(): LiveData<Long> = _navigateToLibrary

    private val _isClickAllowed = MutableLiveData(true)
    private fun observeIsClickAllowed(): LiveData<Boolean> = _isClickAllowed


    init {
        viewModelScope.launch {
            favoriteInteractor.getFavorites().collect {
                _favoriteList.postValue(it)
            }
        }
    }

    fun onSongClickListener(song: Song) {
        if (_isClickAllowed.value ?: false) {
            _isClickAllowed.postValue(false)
            _navigateToLibrary.value = song.trackId
            allowNextRequest()
        }
    }

    private fun allowNextRequest() {
        clickDebounceJob = viewModelScope.launch {
            delay(DEBOUNCE_CLICK)
            _isClickAllowed.postValue(true)
        }
    }

    companion object {
        private const val DEBOUNCE_CLICK = 500L
    }
}