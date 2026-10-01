package com.example.playlistmaker.media.ui.view_model

import android.net.Uri

data class CreatePlaylistState(
    val coverUri: Uri? = null,
    val name: String = "",
    val description: String = ""
) {
    val hasUnsavedData: Boolean
        get() = coverUri != null || name.isNotBlank() || description.isNotBlank()

    val isCreateEnabled: Boolean
        get() = name.isNotBlank()
}