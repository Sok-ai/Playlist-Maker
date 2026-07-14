package com.example.playlistmaker.library.di

import android.media.MediaPlayer
import com.example.playlistmaker.library.data.impl.MediaPlayerImpl
import com.example.playlistmaker.library.domain.api.MusicPlayer
import org.koin.dsl.module

val libraryModule = module {
    factory<MusicPlayer> {
        MediaPlayerImpl(mediaPlayer = get())
    }
    factory<MediaPlayer> { MediaPlayer() }
}