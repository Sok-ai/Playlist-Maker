package com.example.playlistmaker.media.data.db.converter

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.model.Playlist

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