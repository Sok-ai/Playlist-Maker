package com.example.playlistmaker.media.ui.nested_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.core.ui.adapter.SongAdapter
import com.example.playlistmaker.core.ui.dialog.ConfirmationDialog
import com.example.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.example.playlistmaker.library.ui.LibraryFragment
import com.example.playlistmaker.media.ui.view_model.PlaylistDetailViewModel
import com.example.playlistmaker.utils.TimeFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistDetailFragment : BindingFragment<FragmentPlaylistDetailBinding>() {

    private val playlistId by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getLong(PLAYLIST_ID_KEY)
    }

    private val vm: PlaylistDetailViewModel by viewModel<PlaylistDetailViewModel> {
        parametersOf(playlistId)
    }

    private lateinit var bottomSheetSongs: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetAdvancedSetting: BottomSheetBehavior<LinearLayout>
    private var songAdapter: SongAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentPlaylistDetailBinding =
        FragmentPlaylistDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songAdapter = SongAdapter(
            onSongActionListener = { song ->
                findNavController().navigate(
                    R.id.action_playlistDetailFragment_to_libraryFragment,
                    LibraryFragment.createArgs(idSong = song.trackId)
                )
            },
            onSongActionLongListener = { song ->
                showDialogDeleteSong(song.trackId)
            })

        vm.observeCloseEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        vm.observeSongsToPlaylist().observe(viewLifecycleOwner) { songs ->
            songAdapter?.songs = songs
            val resTime = TimeFormatter.formatMinutes(songs.sumOf { it.trackTimeMillis })
            binding.playlistTrackDuration.text =
                resources.getQuantityString(
                    R.plurals.detail_duration_songs,
                    resTime,
                    resTime
                )
            binding.playlistCountTrack.text = resources.getQuantityString(
                R.plurals.media_playlist_amount_songs,
                songs.size,
                songs.size
            )
            binding.playlistHeader.playlistAmountSongs.text = resources.getQuantityString(
                R.plurals.media_playlist_amount_songs,
                songs.size,
                songs.size
            )
        }
        vm.observePlaylist().observe(viewLifecycleOwner) { playlist ->
            playlist ?: return@observe
            with(binding) {
                Glide.with(this@PlaylistDetailFragment)
                    .load(playlist.pathImage?.let {
                        File(it)
                    })
                    .placeholder(R.drawable.ic_placeholder_312)
                    .into(imagePlaylist)
                playlistName.text = playlist.name
                playlistDescription.text = playlist.description

                playlistHeader.playlistName.text = playlist.name
                Glide.with(this@PlaylistDetailFragment)
                    .load(playlist.pathImage?.let {
                        File(it)
                    })
                    .placeholder(R.drawable.ic_placeholder_312)
                    .into(playlistHeader.playlistImageView)
            }
        }
        settingBottomSheetSongs()
        settingsBottomSheetAdvancedInfo()

        binding.recyclerSongs.adapter = songAdapter
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.sharePlaylist.setOnClickListener {
            intentSharePlaylist()
        }
        binding.moreOption.setOnClickListener {
            bottomSheetAdvancedSetting.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetSongs.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun settingBottomSheetSongs() {
        bottomSheetSongs = BottomSheetBehavior.from(binding.bottomSheetSongsLayout).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        with(binding) {
            buttonShare.setOnClickListener {
                intentSharePlaylist()
            }
            buttonDeletePlaylist.setOnClickListener {
                showDialogDeletePlaylist()
            }
            buttonEditPlaylist.setOnClickListener {

            }
        }
    }

    private fun settingsBottomSheetAdvancedInfo() {
        bottomSheetAdvancedSetting =
            BottomSheetBehavior.from(binding.bottomSheetAdvancedSettingsLayout).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetAdvancedSetting.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, newState: Int) {
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
        binding.overlay.setOnClickListener {
            bottomSheetAdvancedSetting.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showDialogDeletePlaylist() {
        ConfirmationDialog.show(
            context = requireContext(),
            title = "",
            message = getString(R.string.detail_message_delete_playlist),
            onPositive = {
                vm.deletePlaylist()
            },
            onNegative = {
            }
        )
    }

    private fun showDialogDeleteSong(id: Long) {
        ConfirmationDialog.show(
            context = requireContext(),
            title = "",
            message = getString(R.string.detail_message_delete_song),
            onPositive = {
                vm.deleteSong(id)
            },
            onNegative = {
            }
        )
    }

    private fun intentSharePlaylist() {
        val shareData = vm.getShareData() ?: return
        if (shareData.tracksCount == 0) {
            Toast.makeText(
                requireContext(),
                R.string.detail_message_share_playlist,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val trackCountText = resources.getQuantityString(
            R.plurals.media_playlist_amount_songs,
            shareData.tracksCount,
            shareData.tracksCount
        )
        val shareText = buildString {
            append(shareData.name).append("\n")
            if (shareData.description.isNotBlank()) {
                append(shareData.description).append("\n")
            }
            append(trackCountText).append("\n")
            append(shareData.tracksList)
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun onDestroyView() {
        binding.recyclerSongs.adapter = null
        songAdapter = null
        super.onDestroyView()
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "playlist_id_key"
        fun createArgs(playlistId: Long) = Bundle().apply {
            putLong(PLAYLIST_ID_KEY, playlistId)
        }

    }
}