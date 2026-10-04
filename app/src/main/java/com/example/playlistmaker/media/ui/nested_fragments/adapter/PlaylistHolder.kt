package com.example.playlistmaker.media.ui.nested_fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.core.domain.model.Playlist
import java.io.File

class PlaylistHolder(private val binding: PlaylistViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        with(binding) {
            Glide
                .with(itemView)
                .load(model.pathImage?.let { File(it) })
                .placeholder(R.drawable.ic_placeholder_45)
                .into(playlistImageView)
            playlistName.text = model.name
            playlistAmountSongs.text = itemView.resources.getQuantityString(
                R.plurals.media_playlist_amount_songs,
                model.songCount,
                model.songCount
            )
        }
    }

    companion object {
        fun instance(parent: ViewGroup): PlaylistHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistViewBinding.inflate(inflater, parent, false)
            return PlaylistHolder(binding)
        }
    }
}