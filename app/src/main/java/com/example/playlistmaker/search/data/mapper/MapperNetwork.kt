package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.search.data.dto.SongDto

class MapperNetwork {
    fun toSong(dto: SongDto) = Song(
        trackId = dto.trackId,
        trackName = dto.trackName,
        artistName = dto.artistName,
        collectionName = dto.collectionName,
        releaseDate = dto.releaseDate,
        primaryGenreName = dto.primaryGenreName,
        country = dto.country,
        trackTimeMillis = dto.trackTimeMillis,
        artworkUrl100 = dto.artworkUrl100,
        previewUrl = dto.previewUrl
    )
}