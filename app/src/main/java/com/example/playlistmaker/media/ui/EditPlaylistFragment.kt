package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment() {
    private val playlistId by lazy { requireArguments().getLong(PLAYLIST_ID_KEY) }

    override val vm: EditPlaylistViewModel by viewModel {
        parametersOf(playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistText.text = getString(R.string.media_title_edit_playlist)
        binding.createPlaylistButton.text = getString(R.string.media_btn_edit_playlist)
    }

    override fun handleBack() {
        findNavController().navigateUp()
    }

    override fun onSaveSuccess(name: String) {
        findNavController().navigateUp()
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "playlist_id_key"
        fun createArgs(playlistId: Long) = Bundle().apply {
            putLong(PLAYLIST_ID_KEY, playlistId)
        }
    }
}