package com.example.playlistmaker.core.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.core.domain.model.Playlist

object PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(old: Playlist, new: Playlist) = old.id == new.id
    override fun areContentsTheSame(old: Playlist, new: Playlist) = old == new
}