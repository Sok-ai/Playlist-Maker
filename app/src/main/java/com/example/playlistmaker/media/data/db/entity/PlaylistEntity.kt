package com.example.playlistmaker.media.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "playlist_name")
    val playlistName: String,
    @ColumnInfo(name = "playlist_description")
    val playlistDescription: String,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "songs")
    val songId: String,
    @ColumnInfo(name = "amount_songs")
    val amountSongs: Int,
)