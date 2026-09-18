package com.example.playlistmaker.media.ui.nested_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.library.ui.LibraryFragment
import com.example.playlistmaker.media.ui.view_model.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {
    private val vm: FavoriteViewModel by viewModel<FavoriteViewModel>()
    private var favoriteAdapter: FavoriteAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.observeNavigateToLibrary().observe(viewLifecycleOwner) {
            openMusic(it)
        }

        favoriteAdapter = FavoriteAdapter { song ->
            vm.onSongClickListener(song)
        }.apply {
            vm.observeFavoriteList().observe(viewLifecycleOwner) { listSong ->
                if (listSong.isNotEmpty()) {
                    favoriteList = listSong
                    showContent()
                } else {
                    showEmptyScreen()
                }
            }
        }

        binding.recyclerFavorites.adapter = favoriteAdapter
    }

    private fun showEmptyScreen() {
        with(binding) {
            recyclerFavorites.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }
    }

    private fun showContent() {
        with(binding) {
            recyclerFavorites.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        binding.recyclerFavorites.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteAdapter = null
    }

    private fun openMusic(songId: Long) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_libraryFragment,
            LibraryFragment.createArgs(songId),
            navOptions {
                launchSingleTop = true
            }
        )
    }

    companion object {
        fun newInstance() =
            FavoriteFragment().apply {
            }
    }
}