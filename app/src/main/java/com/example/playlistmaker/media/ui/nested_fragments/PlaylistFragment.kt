package com.example.playlistmaker.media.ui.nested_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.ui.CreatePlaylistFragment
import com.example.playlistmaker.media.ui.nested_fragments.adapter.PlaylistAdapter
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {
    private val vm: PlaylistViewModel by viewModel<PlaylistViewModel>()
    private var playlistAdapter: PlaylistAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistAdapter = PlaylistAdapter()
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(CreatePlaylistFragment.KEY_PLAYLIST_CREATED)
            ?.observe(viewLifecycleOwner) { playlistName ->
                showPlaylistCreatedMessage(playlistName)

                findNavController()
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>(CreatePlaylistFragment.KEY_PLAYLIST_CREATED)
            }

        with(binding) {
            btnCreatePlaylist.setOnClickListener {
                findNavController()
                    .navigate(
                        R.id.action_mediaFragment_to_createPlaylistFragment
                    )
            }
            recyclerPlaylists.adapter = playlistAdapter
        }
        vm.observePlaylists().observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                playlistAdapter?.submitList(playlists) ?: return@observe
                showContentLayout()
            } else {
                showEmptyLayout()
            }
        }
    }

    private fun showPlaylistCreatedMessage(playlistName: String) {
        if (binding.root.isAttachedToWindow) {
            snackBarCreatedMessage(playlistName)
        } else {
            binding.root.doOnAttach {
                snackBarCreatedMessage(playlistName)
            }
        }
    }

    private fun snackBarCreatedMessage(message: String) {
        Snackbar.make(
            binding.root,
            getString(
                R.string.playlist_created_message,
                message
            ),
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    private fun showContentLayout() {
        with(binding) {
            recyclerPlaylists.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    private fun showEmptyLayout() {
        with(binding) {
            recyclerPlaylists.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        binding.recyclerPlaylists.adapter = null
        playlistAdapter = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() =
            PlaylistFragment().apply {
            }
    }
}