package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Song

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
        holder.itemView.setOnClickListener {
            onSongActionListener?.onSongClick(searchHistoryList[position])
        }
    }

    override fun getItemCount(): Int = searchHistoryList.size
}