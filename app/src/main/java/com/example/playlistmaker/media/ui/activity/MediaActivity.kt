package com.example.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.media.ui.MediaPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : BindingFragment<ActivityMediaBinding>() {
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var pageAdapter: MediaPageAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityMediaBinding = ActivityMediaBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageAdapter = MediaPageAdapter(childFragmentManager, lifecycle)
        binding.pages.adapter = pageAdapter

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.pages) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_name_tag_favorites)
                else -> tab.text = getString(R.string.media_name_tag_playlists)
            }
        }

        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tabLayoutMediator.isInitialized) {
            tabLayoutMediator.detach()
        }
    }
}