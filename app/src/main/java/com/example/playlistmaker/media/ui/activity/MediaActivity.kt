package com.example.playlistmaker.media.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.media.ui.MediaPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var pageAdapter: MediaPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val totalLeft = systemBars.left + cutout.left
            val totalRight = systemBars.right + cutout.right

            v.updatePadding(
                left = v.paddingLeft + totalLeft,
                top = statusBar.top,
                right = v.paddingRight + totalRight,
                bottom = navBar.bottom
            )
            insets
        }
        pageAdapter = MediaPageAdapter(supportFragmentManager, lifecycle)
        binding.pages.adapter = pageAdapter

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.pages) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_name_tag_favorites)
                else -> tab.text = getString(R.string.media_name_tag_playlists)
            }
        }

        tabLayoutMediator.attach()

        binding.btnMediaToMain.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tabLayoutMediator.isInitialized) {
            tabLayoutMediator.detach()
        }
    }
}