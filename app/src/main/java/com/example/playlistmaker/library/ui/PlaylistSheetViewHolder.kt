package com.example.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistSheetViewBinding
import com.example.playlistmaker.core.domain.model.Playlist
import java.io.File

class PlaylistSheetViewHolder(private val binding: PlaylistSheetViewBinding) :
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
        fun createInstance(parent: ViewGroup): PlaylistSheetViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistSheetViewBinding.inflate(inflater, parent, false)
            return PlaylistSheetViewHolder(binding)
        }
    }
}