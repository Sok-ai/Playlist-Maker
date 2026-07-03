package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.utils.PeriodicAction
import com.google.gson.Gson

const val TRACK_ID_KEY = "track_id_key"

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding
    private val periodicUpdater = PeriodicAction(500L)

    private val musicPlayer = Creator.provideMusicPlayer()
    private val songInteractor = Creator.provideSearchInteractor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val totalLeft = systemBars.left + cutout.left
            val totalRight = systemBars.right + cutout.right

            v.updatePadding(
                left = v.paddingLeft + totalLeft,
                top = statusBar.top,
                right = v.paddingRight + totalRight,
                bottom = navBar.bottom
            )
            insets
        }

        gettingMusic()

        binding.playMusicButton.setOnClickListener {
            when {
                musicPlayer.isPlayer() -> {
                    musicPlayer.pausePlayer()
                    binding.playMusicButton.setImageResource(R.drawable.ic_button_start_song)
                    stopUpdatingTime()
                }

                musicPlayer.isPreparedOrPause() -> {
                    musicPlayer.startPlayer()
                    binding.playMusicButton.setImageResource(R.drawable.ic_button_pause_song)
                    startUpdatingTime()
                }
            }
        }

        binding.btnLibraryToMain.setOnClickListener {
            finish()
        }
    }

    private fun gettingMusic() {
        val songId: Long = intent.getLongExtra(TRACK_ID_KEY, 0)

        val resSong = songInteractor.getSongById(songId)

        if (resSong != null) {
            saveIfNewTrack(resSong)
            settingValuesToView(resSong)
        } else {
            loadLastTrack()
        }
    }

    private fun saveIfNewTrack(song: Song) {
        val lastTrack = songInteractor.getLastTrack()
        val lastTrackJson = lastTrack?.let { Gson().toJson(it) } ?: ""
        val newSongJson = Gson().toJson(song)

        if (lastTrackJson != newSongJson) {
            songInteractor.saveLastTrack(song)
        }
    }

    private fun loadLastTrack() {
        val songData = songInteractor.getLastTrack()
        if (songData != null) {
            settingValuesToView(songData)
        } else {
            defaultValueForView()
        }
    }

    private fun settingValuesToView(songData: Song) {
        Glide.with(applicationContext).load(songData.coverImagePlayer)
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(16))
            .into(binding.albumMusicImage)

        binding.nameMusicText.text = songData.trackName
        binding.nameAuthorText.text = songData.artistName

       binding.albumMusicText.text = songData.collectionName
       binding.yearMusicText.text = songData.yearReleaseTrack

        binding.durationMusicText.text = songData.trackTime
        binding.genreMusicText.text = songData.primaryGenreName
        binding.countryMusicText.text = songData.country


        musicPlayer.apply {
            preparePlayer(songData.previewUrl) {
                binding.playMusicButton.isEnabled = true
            }
            setOnCompletionListener {
                binding.playMusicButton.setImageResource(R.drawable.ic_button_start_song)
                stopUpdatingTime()
                binding.timeToPlayText.text = Song.formatDuration(0)
            }
        }
    }

    private fun defaultValueForView() {
        binding.nameMusicText.setText(R.string.default_text)
        binding.nameAuthorText.setText(R.string.default_text)
        binding.albumMusicText.text = ""
        binding.yearMusicText.text = ""
        binding.durationMusicText.text = Song.formatDuration(0)
        binding.genreMusicText.text = ""
        binding.countryMusicText.text = ""
    }


    private fun startUpdatingTime() {
        periodicUpdater.start {
            if (musicPlayer.isPlayer()) {
                binding.timeToPlayText.text = Song.formatDuration(musicPlayer.currentPosition())
            }
        }
    }

    private fun stopUpdatingTime() {
        periodicUpdater.stop()
    }

    override fun onPause() {
        super.onPause()
        if (musicPlayer.isPlayer()) {
            musicPlayer.pausePlayer()
            binding.playMusicButton.setImageResource(R.drawable.ic_button_start_song)
            stopUpdatingTime()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.release()
    }
}