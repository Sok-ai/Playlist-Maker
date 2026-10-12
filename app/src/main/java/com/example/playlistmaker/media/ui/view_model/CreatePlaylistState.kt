package com.example.playlistmaker.media.ui.view_model

import android.net.Uri

data class CreatePlaylistState(
    val id: Long = 0,
    val coverUri: Uri? = null,
    val existingCoverPath: String? = null,
    val name: String = "",
    val description: String = "",
    val songCount: Int = 0
) {
    val hasUnsavedData: Boolean
        get() = coverUri != null || name.isNotBlank() || description.isNotBlank()

    val isCreateEnabled: Boolean
        get() = name.isNotBlank()
}