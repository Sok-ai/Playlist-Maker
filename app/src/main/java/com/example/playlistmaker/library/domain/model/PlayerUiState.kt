package com.example.playlistmaker.library.domain.model

import com.example.playlistmaker.core.domain.model.Song

data class PlayerUiState(
    val isLoading: Boolean = false,
    val song: Song? = null,
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val isFavorite: Boolean = false
)