package com.example.playlistmaker.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Song

class SongAdapter(val onSongActionListener: OnSongActionListener? = null) :
    RecyclerView.Adapter<SongViewHolder>() {
    var songs: MutableList<Song> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SongViewHolder {
        return SongViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: SongViewHolder, position: Int
    ) {
        holder.bind(songs[position])
        holder.itemView.setOnClickListener {
            onSongActionListener?.onSongClick(songs[position])
        }
    }

    override fun getItemCount(): Int = songs.size
}
