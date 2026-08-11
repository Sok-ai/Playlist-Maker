package com.example.playlistmaker.media.ui.nested_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.media.ui.view_model.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {
    private val vm: FavoriteViewModel by viewModel<FavoriteViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun newInstance() =
            FavoriteFragment().apply {
            }
    }
}