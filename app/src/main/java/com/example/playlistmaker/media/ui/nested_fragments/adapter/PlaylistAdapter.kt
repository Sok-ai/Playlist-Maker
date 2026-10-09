package com.example.playlistmaker.media.ui.nested_fragments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.core.domain.model.Playlist
import com.example.playlistmaker.core.ui.OnPlaylistActionListener
import com.example.playlistmaker.core.ui.adapter.diff.PlaylistDiffCallback

class PlaylistAdapter(private val onPlaylistActionListener: OnPlaylistActionListener? = null) : ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder = PlaylistViewHolder.createInstance(parent)

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onPlaylistActionListener?.onPlaylistClick(getItem(position))
        }
    }
}