package com.example.playlistmaker.core.data.converter

import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.core.domain.model.Playlist

class PlaylistConverter {
    fun toPlaylistEntity(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            imagePath = playlist.pathImage,
            songId = "",
            amountSongs = playlist.songCount
        )

    fun toPlaylist(playlistEntity: PlaylistEntity): Playlist =
        Playlist(
            id = playlistEntity.id,
            name = playlistEntity.playlistName,
            description = playlistEntity.playlistDescription,
            pathImage = playlistEntity.imagePath,
            songs = emptyList(),
            songCount = playlistEntity.amountSongs
        )
}