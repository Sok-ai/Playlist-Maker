package com.example.playlistmaker

import android.content.SharedPreferences
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
import com.example.playlistmaker.player.MediaPlayerImpl
import com.example.playlistmaker.utils.PeriodicAction
import com.google.gson.Gson

const val TRACK_ID_KEY = "track_id_key"
const val SONG_LIBRARY_KEY = "song_library_key"

class LibraryActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var searchHistory: SearchHistory
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
    private val periodicUpdater = PeriodicAction(500L)
    private val mediaPlayerImpl = MediaPlayerImpl()

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
        gettingMusic()

        playMusicButton.setOnClickListener {
            when {
                mediaPlayerImpl.isPlayer() -> {
                    mediaPlayerImpl.pausePlayer()
                    playMusicButton.setImageResource(R.drawable.ic_button_start_song)
                    stopUpdatingTime()
                }

                mediaPlayerImpl.isPreparedOrPause() -> {
                    mediaPlayerImpl.startPlayer()
                    playMusicButton.setImageResource(R.drawable.ic_button_pause_song)
                    startUpdatingTime()
                }
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPref)

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
        val songId: Long = intent.getLongExtra(TRACK_ID_KEY, 0)

        val resSong = searchHistory.getSongById(songId)

        if (resSong != null) {
            saveIfNewTrack(resSong)
            settingValuesToView(resSong)
        } else {
            loadLastTrack()
        }
    }

    private fun saveIfNewTrack(song: Song) {
        val currentSavedJson = sharedPref.getString(SONG_LIBRARY_KEY, "")
        val newSongJson = Gson().toJson(song)

        if (currentSavedJson != newSongJson) {
            sharedPref.edit().putString(SONG_LIBRARY_KEY, newSongJson).apply()
        }
    }

    private fun loadLastTrack() {
        val savedJson = sharedPref.getString(SONG_LIBRARY_KEY, "")
        if (savedJson?.isNotEmpty() == true) {
            val songData = Gson().fromJson(savedJson, Song::class.java)
            settingValuesToView(songData)
        } else {
            defaultValueForView()
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


        mediaPlayerImpl.apply {
            preparePlayer(songData.previewUrl) {
                playMusicButton.isEnabled = true
            }
            setOnCompletionListener {
                playMusicButton.setImageResource(R.drawable.ic_button_start_song)
                stopUpdatingTime()
                timeToPlayText.text = Song.formatDuration(0)
            }
        }
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


    private fun startUpdatingTime() {
        periodicUpdater.start {
            if (mediaPlayerImpl.isPlayer()) {
                timeToPlayText.text = Song.formatDuration(mediaPlayerImpl.currentPosition())
            }
        }
    }

    private fun stopUpdatingTime() {
        periodicUpdater.stop()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayerImpl.isPlayer()) {
            mediaPlayerImpl.pausePlayer()
            playMusicButton.setImageResource(R.drawable.ic_button_start_song)
            stopUpdatingTime()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerImpl.release()
    }
}