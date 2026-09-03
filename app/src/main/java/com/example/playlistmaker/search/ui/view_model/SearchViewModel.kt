package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.model.SearchResult.*
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class SearchViewModel(private val interactor: SearchInteractor) : ViewModel() {
    private var searchDebounceJob: Job? = null
    private var clickDebounceJob: Job? = null

    private val executer = Executors.newCachedThreadPool()

    private val _searchHistory = MutableLiveData(interactor.getHistory())
    fun observeHistory(): LiveData<List<Song>> = _searchHistory

    private val _songsList = MutableLiveData<List<Song>>()
    fun observeSongsList(): LiveData<List<Song>> = _songsList

    private val _isClickAllowed = MutableLiveData(true)
    fun observeDebounceClick(): LiveData<Boolean> = _isClickAllowed

    private val _navigationToPlayer = SingleLiveEvent<Long>()
    fun observeNavigationToPlayer(): LiveData<Long> = _navigationToPlayer

    private val _stateNetwork = MutableLiveData<SearchResult>()
    fun observeStateNetwork(): LiveData<SearchResult> = _stateNetwork

    private var lastRequest: String? = null

    fun addToHistory(song: Song) {
        interactor.addToHistory(song)
        _searchHistory.postValue(interactor.getHistory())
    }

    fun clearHistory() {
        interactor.clearHistory()
        _searchHistory.postValue(interactor.getHistory())
    }

    fun clearSongsList() {
        _songsList.postValue(emptyList())
    }

    fun onSongHistoryClicked(song: Song) {
        if (_isClickAllowed.value ?: false) {
            _isClickAllowed.postValue(false)
            _navigationToPlayer.value = song.trackId
            allowNextRequest()
        }
    }

    fun onSongClicked(song: Song) {
        if (_isClickAllowed.value ?: false) {
            addToHistory(song)
            _isClickAllowed.postValue(false)
            _navigationToPlayer.value = song.trackId
            allowNextRequest()
        }
    }

    private fun allowNextRequest() {
        clickDebounceJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE)
            _isClickAllowed.postValue(true)
        }
    }

    fun repeatRequest() {
        searchSongs(lastRequest ?: "")
    }

    fun requestToNetwork(inputText: String) {
        if (lastRequest != inputText && inputText.isNotEmpty()) {
            this.lastRequest = inputText

            searchDebounceJob?.cancel()

            searchDebounceJob = viewModelScope.launch {
                delay(SEARCH_DELAY_DEBOUNCE)
                searchSongs(inputText)
            }
        } else {
            searchDebounceJob?.cancel()
        }
    }

    private fun stateSetup(state: SearchResult) {
        _stateNetwork.postValue(state)
    }

    fun searchSongs(input: String) {
        if (input.isNotEmpty()) {
            stateSetup(Loading)
            executer.execute {
                interactor.searchSongs(input) { result ->
                    when (result) {
                        is Success -> {
                            if (result.songs.isNotEmpty()) {
                                _songsList.postValue(result.songs)
                                stateSetup(Success(result.songs))
                            } else {
                                stateSetup(Empty)
                            }
                        }

                        is Empty -> stateSetup(Empty)
                        is Error -> {
                            stateSetup(Error)
                        }

                        Loading -> {}
                    }
                }
            }
        }
    }

    companion object {
        const val SEARCH_DELAY_DEBOUNCE = 2000L
        const val CLICK_DEBOUNCE = 500L
    }
}