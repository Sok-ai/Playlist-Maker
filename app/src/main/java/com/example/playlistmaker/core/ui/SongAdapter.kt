package com.example.playlistmaker.core.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.core.domain.model.Song

class SongAdapter(val onSongActionListener: OnSongActionListener? = null) :
    RecyclerView.Adapter<SongViewHolder>() {

    var songs = emptyList<Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongViewHolder = SongViewHolder.createInstance(parent)

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int
    ) {
        holder.bind(songs[position])
        holder.itemView.setOnClickListener {
            onSongActionListener?.onSongClick(songs[position])
        }
    }

    override fun getItemCount(): Int = songs.size
}