package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.ui.BindingFragment
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {
    private val vm: PlaylistViewModel by viewModel<PlaylistViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
            btnCreatePlaylist.setOnClickListener { }
        }
    }

    companion object {
        fun newInstance() =
            PlaylistFragment().apply {
//                arguments = Bundle().apply {
//
//                }
            }
    }
}