package com.example.playlistmaker.core.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "playlist_track_ref",
    primaryKeys = ["ref_playlist_id", "ref_song_id"],
    foreignKeys = [
        ForeignKey(
            PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["ref_playlist_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            SongInPlaylistEntity::class,
            parentColumns = ["song_id"],
            childColumns = ["ref_song_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("ref_song_id")]
)
data class PlaylistTrackRefEntity(
    @ColumnInfo(name = "ref_playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "ref_song_id")
    val songId: Long,
    @ColumnInfo(name = "added_at")
    val addedAt: Long
)