package com.example.playlistmaker.media.ui.nested_fragments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.core.ui.OnSongActionListener

class FavoriteAdapter(val onSongActionListener: OnSongActionListener? = null) :
    RecyclerView.Adapter<FavoriteViewHolder>() {

    var favoriteList = emptyList<Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder = FavoriteViewHolder.createInstance(parent)

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int
    ) {
        holder.bind(favoriteList[position])
        holder.itemView.setOnClickListener {
            onSongActionListener?.onSongClick(favoriteList[position])
        }
    }

    override fun getItemCount(): Int = favoriteList.size
}