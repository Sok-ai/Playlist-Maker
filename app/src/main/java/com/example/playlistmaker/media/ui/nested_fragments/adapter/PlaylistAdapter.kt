package com.example.playlistmaker.media.ui.nested_fragments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistAdapter : ListAdapter<Playlist, PlaylistHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistHolder = PlaylistHolder.instance(parent)

    override fun onBindViewHolder(
        holder: PlaylistHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(
                oldItem: Playlist,
                newItem: Playlist
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Playlist,
                newItem: Playlist
            ): Boolean = oldItem == newItem
        }
    }
}