package com.example.playlistmaker.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.databinding.SongViewBinding
import com.example.playlistmaker.utils.TimeFormatter

class SongViewHolder(private val binding: SongViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Song) {
        with(binding) {
            Glide
                .with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .transform(RoundedCorners(2))
                .into(trackImage)
            trackName.text = model.trackName
            trackNameArtist.text = model.artistName
            trackTime.text = TimeFormatter.format(model.trackTimeMillis)
        }
    }

    companion object {
        fun createInstance(parent: ViewGroup): SongViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SongViewBinding.inflate(layoutInflater, parent, false)
            return SongViewHolder(binding)
        }
    }
}