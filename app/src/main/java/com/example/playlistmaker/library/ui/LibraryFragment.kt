package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.library.ui.view_model.LibraryViewModel
import com.example.playlistmaker.search.domain.model.Song
import com.example.playlistmaker.search.domain.model.Song.Companion.formatDuration
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class LibraryFragment : BindingFragment<ActivityLibraryBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityLibraryBinding = ActivityLibraryBinding.inflate(inflater, container, false)

    private val trackId by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getLong(TRACK_ID_KEY)
    }
    private val viewModel: LibraryViewModel by viewModel<LibraryViewModel> { parametersOf(trackId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeUiState().observe(viewLifecycleOwner)
        { uiState ->
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
            }
        }

        binding.playMusicButton.setOnClickListener {
            viewModel.onClickPlayer()
        }

        binding.btnLibraryToMain.setOnClickListener {
            findNavController().navigateUp()
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
        val radius = requireContext().dpToPx(16f)
        Glide.with(this).load(songData.coverImagePlayer)
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(radius))
            .into(binding.albumMusicImage)

        binding.nameMusicText.text = songData.trackName
        binding.nameAuthorText.text = songData.artistName

        binding.albumMusicText.text = songData.collectionName
        binding.yearMusicText.text = songData.yearReleaseTrack

        binding.durationMusicText.text = songData.trackTime
        binding.genreMusicText.text = songData.primaryGenreName
        binding.countryMusicText.text = songData.country
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        const val TRACK_ID_KEY = "track_id_key"
        fun createArgs(idSong: Long) = Bundle().apply {
            putLong(TRACK_ID_KEY, idSong)
        }
    }
}