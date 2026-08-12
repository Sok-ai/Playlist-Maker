package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.App
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<ActivitySettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivitySettingsBinding = ActivitySettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeTheme().observe(viewLifecycleOwner) { isDark ->
            binding.themeSwitcher.isChecked = isDark
            (requireContext().applicationContext as App).switchTheme(isDark)
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

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeThemeApp(checked)
        }
    }
}