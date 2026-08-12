package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            binding.fragmentContainerView.getFragment<NavHostFragment>().navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val bottomNavView = binding.bottomNavigation
            when (destination.id) {
                R.id.searchFragment, R.id.mediaFragment, R.id.settingsFragment -> {
                    bottomNavView.visibility = VISIBLE
                    bottomNavView.menu.findItem(destination.id)?.isChecked = true
                }

                else -> {
                    bottomNavView.visibility = GONE
                }
            }
        }
        binding.bottomNavigation.setupWithNavController(navController)
    }
}