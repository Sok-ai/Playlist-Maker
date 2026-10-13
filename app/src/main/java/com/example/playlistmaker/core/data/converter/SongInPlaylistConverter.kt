package com.example.playlistmaker.core.data.converter

import com.example.playlistmaker.core.data.db.entity.SongInPlaylistEntity
import com.example.playlistmaker.core.domain.model.Song

class SongInPlaylistConverter {
    fun toEntity(song: Song): SongInPlaylistEntity = SongInPlaylistEntity(
        trackId = song.trackId,
        trackName = song.trackName,
        artistName = song.artistName,
        collectionName = song.collectionName,
        releaseDate = song.releaseDate,
        primaryGenreName = song.primaryGenreName,
        country = song.country,
        trackTimeMillis = song.trackTimeMillis,
        artworkUrl100 = song.artworkUrl100,
        previewUrl = song.previewUrl
    )

    fun toSong(songEntity: SongInPlaylistEntity): Song =
        Song(
            trackId = songEntity.trackId,
            trackName = songEntity.trackName,
            artistName = songEntity.artistName,
            collectionName = songEntity.collectionName,
            releaseDate = songEntity.releaseDate,
            primaryGenreName = songEntity.primaryGenreName,
            country = songEntity.country,
            trackTimeMillis = songEntity.trackTimeMillis,
            artworkUrl100 = songEntity.artworkUrl100,
            previewUrl = songEntity.previewUrl
        )
}