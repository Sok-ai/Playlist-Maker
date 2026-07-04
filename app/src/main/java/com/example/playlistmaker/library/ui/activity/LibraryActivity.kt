package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.library.ui.view_model.LibraryViewModel
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.search.domain.model.Song.Companion.formatDuration

const val TRACK_ID_KEY = "track_id_key"

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding
    private lateinit var viewModel: LibraryViewModel

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

        val songId = intent.getLongExtra(TRACK_ID_KEY, 0)
        viewModel =
            ViewModelProvider(
                this,
                LibraryViewModel.getFactory(
                    Creator.provideMusicPlayer(),
                    Creator.provideSearchInteractor(),
                    songId
                )
            )[LibraryViewModel::class.java]

        viewModel.observeUiState().observe(this) { uiState ->
            binding.timeToPlayText.text = formatDuration(uiState.currentPosition)
            showUi(uiState.isLoading)
            if (uiState.isReady) {
                binding.playMusicButton.isEnabled = true
                uiState.song?.let {
                    settingValuesToView(it)
                }

                if (uiState.isPlaying) {
                    binding.playMusicButton.setImageResource(R.drawable.ic_button_pause_song)
                } else {
                    binding.playMusicButton.setImageResource(R.drawable.ic_button_start_song)
                }
            } else {
                defaultValueForView()
            }
        }

        binding.playMusicButton.setOnClickListener {
            viewModel.onClickPlayer()
        }

        binding.btnLibraryToMain.setOnClickListener {
            finish()
        }
    }

    private fun showUi(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                progressBarSong.visibility = View.VISIBLE
                contentGroup.visibility = View.GONE
            }
        } else {
            with(binding) {
                progressBarSong.visibility = View.GONE
                contentGroup.visibility = View.VISIBLE
            }
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
    }

    private fun defaultValueForView() {
        binding.nameMusicText.setText(R.string.default_text)
        binding.nameAuthorText.setText(R.string.default_text)
        binding.albumMusicText.text = ""
        binding.yearMusicText.text = ""
        binding.durationMusicText.text = formatDuration(0)
        binding.genreMusicText.text = ""
        binding.countryMusicText.text = ""
        binding.playMusicButton.isEnabled = false
        binding.playMusicButton.setImageResource(R.drawable.ic_button_start_song)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}