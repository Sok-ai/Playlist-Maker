package com.example.playlistmaker.library.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.ui.adapter.diff.PlaylistDiffCallback

class PlaylistSheetAdapter : ListAdapter<Playlist, PlaylistSheetViewHolder>(PlaylistDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistSheetViewHolder = PlaylistSheetViewHolder.createInstance(parent)

    override fun onBindViewHolder(
        holder: PlaylistSheetViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}