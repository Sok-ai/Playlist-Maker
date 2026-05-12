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
import com.google.gson.Gson

const val MUSIC_TRANSFER_KEY = "music_transfer_key"
const val SONG_LIBRARY_KEY = "song_library_key"

class LibraryActivity : AppCompatActivity() {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(
                top = statusBar.top,
                right = statusBar.right,
                left = statusBar.left,
                bottom = statusBar.bottom
            )
            insets
        }

        initView()

        val fromPlayer = intent.getBooleanExtra("from_player", false)

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
        val jsonSong = intent.getStringExtra(MUSIC_TRANSFER_KEY)
        if (jsonSong != null) {
            sharedPref.edit().putString(SONG_LIBRARY_KEY, jsonSong).apply()
            val songData = Gson().fromJson(jsonSong, Song::class.java)
            settingValuesToView(songData)
        }
    }

    private fun settingValuesToView(songData: Song) {
        Glide.with(applicationContext).load(songData.coverImagePlayer)
            .placeholder(R.drawable.ic_placeholder_312).into(albumMusicImage)

        nameMusicText.text = songData.trackName
        nameAuthorText.text = songData.artistName

        albumMusicText.text = songData.collectionName
        yearMusicText.text = songData.yearReleaseTrack

        durationMusicText.text = songData.trackTime
        genreMusicText.text = songData.primaryGenreName
        countryMusicText.text = songData.country
    }

    private fun defaultValueForView() {
        nameMusicText.setText(R.string.default_text)
        nameAuthorText.setText(R.string.default_text)
        albumMusicText.text = ""
        yearMusicText.text = ""
        durationMusicText.text = "00:00"
        genreMusicText.text = ""
        countryMusicText.text = ""
    }
}