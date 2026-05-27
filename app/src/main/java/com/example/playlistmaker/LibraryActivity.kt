package com.example.playlistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
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
import com.example.playlistmaker.SearchActivity.Companion.FROM_PLAYER_KEY
import com.example.playlistmaker.utils.PeriodicAction
import com.google.gson.Gson

const val MUSIC_TRANSFER_KEY = "music_transfer_key"
const val SONG_LIBRARY_KEY = "song_library_key"

class LibraryActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayer()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var buttonBack: ImageButton
    private lateinit var albumMusicImage: ImageView
    private lateinit var nameMusicText: TextView
    private lateinit var nameAuthorText: TextView
    private lateinit var addPlayListButton: ImageButton
    private lateinit var playMusicButton: ImageButton
    private lateinit var likeMusicButton: ImageButton
    private lateinit var timeToPlayText: TextView
    private lateinit var durationMusicText: TextView
    private lateinit var albumMusicText: TextView
    private lateinit var yearMusicText: TextView
    private lateinit var genreMusicText: TextView
    private lateinit var countryMusicText: TextView
    private var playerState = STATE_DEFAULT
    private val periodicDebounce = PeriodicAction(500L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(top = statusBar.top, bottom = navBar.bottom)
            insets
        }

        initView()

        val fromPlayer = intent.getBooleanExtra(FROM_PLAYER_KEY, false)

        if (fromPlayer) {
            gettingMusic()
        } else {
            val savedJson = sharedPref.getString(SONG_LIBRARY_KEY, "")
            if (savedJson?.isNotEmpty() == true) {
                val songData = Gson().fromJson(savedJson, Song::class.java)
                settingValuesToView(songData)
            } else {
                defaultValueForView()
            }
        }

        playMusicButton.setOnClickListener {
            playbackControl()
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        albumMusicImage = findViewById(R.id.albumMusicImage)
        buttonBack = findViewById(R.id.btn_library_to_main)
        nameMusicText = findViewById(R.id.nameMusicText)
        nameAuthorText = findViewById(R.id.nameAuthorText)
        addPlayListButton = findViewById(R.id.addPlayListButton)
        playMusicButton = findViewById(R.id.playMusicButton)
        likeMusicButton = findViewById(R.id.likeMusicButton)
        timeToPlayText = findViewById(R.id.timeToPlayText)
        durationMusicText = findViewById(R.id.durationMusicText)
        albumMusicText = findViewById(R.id.albumMusicText)
        yearMusicText = findViewById(R.id.yearMusicText)
        genreMusicText = findViewById(R.id.genreMusicText)
        countryMusicText = findViewById(R.id.countryMusicText)
    }

    private fun gettingMusic() {
        val songData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MUSIC_TRANSFER_KEY, Song::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(MUSIC_TRANSFER_KEY)
        }

        if (songData != null) {
            sharedPref.edit().putString(SONG_LIBRARY_KEY, Gson().toJson(songData)).apply()
            settingValuesToView(songData)
        }
    }

    private fun settingValuesToView(songData: Song) {
        Glide.with(applicationContext).load(songData.coverImagePlayer)
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(16))
            .into(albumMusicImage)

        nameMusicText.text = songData.trackName
        nameAuthorText.text = songData.artistName

        albumMusicText.text = songData.collectionName
        yearMusicText.text = songData.yearReleaseTrack

        durationMusicText.text = songData.trackTime
        genreMusicText.text = songData.primaryGenreName
        countryMusicText.text = songData.country

        preparePlayer(songData.previewUrl)
    }

    private fun defaultValueForView() {
        nameMusicText.setText(R.string.default_text)
        nameAuthorText.setText(R.string.default_text)
        albumMusicText.text = ""
        yearMusicText.text = ""
        durationMusicText.text = Song.formatDuration(0)
        genreMusicText.text = ""
        countryMusicText.text = ""
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setVolume(0.6f, 0.6f)
        mediaPlayer.setOnPreparedListener {
            playMusicButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playMusicButton.setImageResource(R.drawable.ic_button_start_song)
            playerState = STATE_PREPARED
            stopUpdatingTime()
            timeToPlayText.text = Song.formatDuration(30)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playMusicButton.setImageResource(R.drawable.ic_button_pause_song)
        playerState = STATE_PLAYING
        startUpdatingTime()
        timeToPlayText.text = ""
    }

    private fun pausePlayer() {
        playMusicButton.setImageResource(R.drawable.ic_button_start_song)
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        stopUpdatingTime()
    }

    private fun startUpdatingTime() {
        periodicDebounce.start {
            if (playerState == STATE_PLAYING) {
                timeToPlayText.text = Song.formatDuration(mediaPlayer.currentPosition)
            }
        }
    }

    private fun stopUpdatingTime() {
        periodicDebounce.stop()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        periodicDebounce.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}