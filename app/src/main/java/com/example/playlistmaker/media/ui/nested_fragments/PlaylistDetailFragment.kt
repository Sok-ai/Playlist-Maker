package com.example.playlistmaker.media.ui.nested_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.example.playlistmaker.media.ui.view_model.PlaylistDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment : BindingFragment<FragmentPlaylistDetailBinding>() {

    private val playlistId by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getLong(PLAYLIST_ID_KEY)
    }

    private val vm: PlaylistDetailViewModel by viewModel<PlaylistDetailViewModel> {
        parametersOf(playlistId)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistDetailBinding =
        FragmentPlaylistDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val PLAYLIST_ID_KEY = "track_id_key"
        fun createArgs(playlistId: Long) = Bundle().apply {
            putLong(PLAYLIST_ID_KEY, playlistId)
        }

    }
}