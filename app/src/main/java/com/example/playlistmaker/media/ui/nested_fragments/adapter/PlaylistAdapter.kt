package com.example.playlistmaker.media.ui.nested_fragments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.ui.adapter.diff.PlaylistDiffCallback

class PlaylistAdapter : ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder = PlaylistViewHolder.createInstance(parent)

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}