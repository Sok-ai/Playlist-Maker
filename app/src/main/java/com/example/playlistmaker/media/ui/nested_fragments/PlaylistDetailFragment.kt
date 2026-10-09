package com.example.playlistmaker.media.ui.nested_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.core.ui.adapter.SongAdapter
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
                playlistCountTrack.text = resources.getQuantityString(
                    R.plurals.media_playlist_amount_songs,
                    playlist.songCount,
                    playlist.songCount
                )
            }
        }
        val bottomSheetSongs = BottomSheetBehavior.from(binding.bottomSheetSongsLayout).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val bottomSheetAdvancedSetting =
            BottomSheetBehavior.from(binding.bottomSheetAdvancedSettingsLayout).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        binding.recyclerSongs.adapter = songAdapter
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
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