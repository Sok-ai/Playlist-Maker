package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.model.SearchResult.*
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.utils.Debounce
import com.example.playlistmaker.utils.SingleLiveEvent

private typealias SearchDebounce = Debounce
private typealias ClickDebounce = Debounce

class SearchViewModel(private val interactor: SearchInteractor) : ViewModel() {
    private val searchDebounce = SearchDebounce(2000)
    private val clickDebounce = ClickDebounce(500)

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
            clickDebounce.run {
                _isClickAllowed.postValue(true)
            }
        }
    }

    fun onSongClicked(song: Song) {
        if (_isClickAllowed.value ?: false) {
            addToHistory(song)
            _isClickAllowed.postValue(false)
            _navigationToPlayer.value = song.trackId
            clickDebounce.run {
                _isClickAllowed.postValue(true)
            }
        }
    }

    fun repeatRequest() {
        searchSongs(lastRequest ?: "")
    }

    fun requestToNetwork(inputText: String) {
        if (lastRequest == inputText) {
            return
        }

        this.lastRequest = inputText

        searchDebounce.cancel()

        searchDebounce.run {
            searchSongs(inputText)
        }
    }

    private fun stateSetup(state: SearchResult) {
        _stateNetwork.postValue(state)
    }

    fun searchSongs(input: String) {
        if (input.isNotEmpty()) {
            stateSetup(Loading)

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

    fun cancelRequest() {
        searchDebounce.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        clickDebounce.cancel()
        searchDebounce.cancel()
    }

    companion object {
        fun getFactory(searchInteractor: SearchInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(searchInteractor)
                }
            }
    }
}