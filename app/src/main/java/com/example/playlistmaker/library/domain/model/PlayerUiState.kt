package com.example.playlistmaker.library.domain.model

import com.example.playlistmaker.search.domain.model.Song

data class PlayerUiState(
    val isLoading: Boolean = false,
    val song: Song? = null,
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
)