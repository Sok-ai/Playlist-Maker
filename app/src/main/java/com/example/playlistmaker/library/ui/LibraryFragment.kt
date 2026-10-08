package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.library.ui.view_model.LibraryViewModel
import com.example.playlistmaker.core.domain.model.Song
import com.example.playlistmaker.library.ui.view_model.AddStatus
import com.example.playlistmaker.utils.TimeFormatter
import com.example.playlistmaker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LibraryFragment : BindingFragment<FragmentLibraryBinding>() {
    private val trackId by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getLong(TRACK_ID_KEY)
    }
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>
    private val viewModel: LibraryViewModel by viewModel<LibraryViewModel> { parametersOf(trackId) }
    private var playlistSheetAdapter: PlaylistSheetAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLibraryBinding = FragmentLibraryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistSheetAdapter = PlaylistSheetAdapter {
            viewModel.addSongToPlaylist(it)
        }

        viewModel.observeAddStatus().observe(viewLifecycleOwner) { status ->
            when (status) {
                is AddStatus.Success -> {
                    showMessageToast(
                        R.string.library_added_playlist_text,
                        status.playlistName
                    )
                    bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                }


                is AddStatus.AlreadyAdded -> showMessageToast(
                    R.string.library_not_added_playlist_text,
                    status.playlistName
                )
            }
        }


        viewModel.observePlaylists().observe(viewLifecycleOwner) { newList ->
            playlistSheetAdapter?.submitList(newList) ?: return@observe
        }

        viewModel.observeUiState().observe(viewLifecycleOwner)
        { uiState ->
            binding.timeToPlayText.text = TimeFormatter.format(uiState.currentPosition.toLong())
            showFavorites(uiState.isFavorite)
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

        settingBottomSheetView()

        binding.likeMusicButton.setOnClickListener {
            viewModel.onClickFavorite()
        }

        binding.playMusicButton.setOnClickListener {
            viewModel.onClickPlayer()
        }

        binding.btnLibraryToMain.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showMessageToast(@StringRes res: Int, playlistName: String) {
        Toast.makeText(
            requireContext(),
            getString(res, playlistName),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun settingBottomSheetView() {
        bottomSheet = BottomSheetBehavior
            .from(binding.bottomSheetLayout)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.bottomSheetCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_createPlaylistFragment)
        }

        binding.addPlayListButton.setOnClickListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.bottomSheetRecycler.adapter = playlistSheetAdapter
    }

    private fun showFavorites(isFavorite: Boolean) = if (isFavorite) {
        binding.likeMusicButton.setImageResource(R.drawable.ic_button_like_full)
    } else {
        binding.likeMusicButton.setImageResource(R.drawable.ic_button_like_empty)
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
        val converterImageForPlayer = converterImagePlayer(songData)
        Glide.with(this).load(converterImageForPlayer)
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(radius))
            .into(binding.albumMusicImage)

        binding.nameMusicText.text = songData.trackName
        binding.nameAuthorText.text = songData.artistName

        binding.albumMusicText.text = songData.collectionName
        binding.yearMusicText.text = songData.releaseDate.toYearOrEmpty()

        binding.durationMusicText.text = TimeFormatter.format(songData.trackTimeMillis)
        binding.genreMusicText.text = songData.primaryGenreName
        binding.countryMusicText.text = songData.country
    }

    fun String.toYearOrEmpty(): String =
        if (length >= 4) substring(0, 4) else ""

    private fun converterImagePlayer(song: Song): String {
        return song.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        binding.bottomSheetRecycler.adapter = null
        playlistSheetAdapter = null
        super.onDestroyView()
    }

    companion object {
        const val TRACK_ID_KEY = "track_id_key"
        fun createArgs(idSong: Long) = Bundle().apply {
            putLong(TRACK_ID_KEY, idSong)
        }
    }
}