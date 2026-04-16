package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SongAdapter() : RecyclerView.Adapter<SongViewHolder>() {
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
    }

    override fun getItemCount(): Int = songs.size
}
