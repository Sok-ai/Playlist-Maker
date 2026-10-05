package com.example.playlistmaker.core.data.converter

import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.core.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConverter {
    fun toPlaylistEntity(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = playlist.id,
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            imagePath = playlist.pathImage,
            songId = Gson().toJson(playlist.songs),
            amountSongs = playlist.songCount
        )

    fun toPlaylist(playlistEntity: PlaylistEntity): Playlist =
        Playlist(
            id = playlistEntity.id,
            name = playlistEntity.playlistName,
            description = playlistEntity.playlistDescription,
            pathImage = playlistEntity.imagePath,
            songs = parseSongs(playlistEntity.songId),
            songCount = playlistEntity.amountSongs
        )


    private fun parseSongs(json: String): List<Long> =
        if (json.isBlank()) emptyList()
        else Gson().fromJson(json, object : TypeToken<List<Long>>() {}.type)
}