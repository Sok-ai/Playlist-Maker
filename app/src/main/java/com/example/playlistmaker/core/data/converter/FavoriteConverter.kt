package com.example.playlistmaker.core.data.converter

import com.example.playlistmaker.core.data.db.entity.SongEntity
import com.example.playlistmaker.core.domain.model.Song

class FavoriteConverter {
    fun toSongEntity(song: Song): SongEntity =
        SongEntity(
            trackId = song.trackId,
            trackName = song.trackName,
            artistName = song.artistName,
            collectionName = song.collectionName,
            releaseDate = song.releaseDate,
            primaryGenreName = song.primaryGenreName,
            country = song.country,
            trackTimeMillis = song.trackTimeMillis,
            artworkUrl100 = song.artworkUrl100,
            previewUrl = song.previewUrl,
            addTime = System.currentTimeMillis()
        )

    fun toSong(songEntity: SongEntity): Song =
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