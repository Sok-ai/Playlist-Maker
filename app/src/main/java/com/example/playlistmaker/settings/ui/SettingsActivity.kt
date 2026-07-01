package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private val themeInteractor = Creator.provideThemeInteractor()
    private val sharingInteractor = Creator.provideSharingInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(top = statusBar.top, bottom = navBar.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory(
                themeInteractor,
                sharingInteractor
            )
        )[SettingsViewModel::class.java]

        viewModel.observeTheme().observe(this) { isDark ->
            binding.themeSwitcher.isChecked = isDark
            (applicationContext as App).switchTheme(isDark)
        }

        binding.settingShare.setOnClickListener {
            viewModel.openCourse()
        }

        binding.settingSupport.setOnClickListener {
            viewModel.contactSupportToEmail()
        }

        binding.settingUserAgreement.setOnClickListener {
            viewModel.openOffer()
        }

        binding.btnSettingsToMain.setOnClickListener {
            finish()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeThemeApp(checked)
        }
    }
}