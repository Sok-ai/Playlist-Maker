package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(val onSongActionListener: OnSongActionListener? = null) :
    RecyclerView.Adapter<SongViewHolder>() {
    var searchHistoryList = emptyList<Song>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongViewHolder = SongViewHolder(parent)

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int
    ) {
        holder.bind(searchHistoryList[position])
    }

    override fun getItemCount(): Int = searchHistoryList.size
}