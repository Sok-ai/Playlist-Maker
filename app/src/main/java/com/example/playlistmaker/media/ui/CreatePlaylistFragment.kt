package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {
    private val vm: CreatePlaylistViewModel by viewModel()
    private var textWatcher: TextWatcher? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreatePlaylistBinding =
        FragmentCreatePlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                vm.onNameChange(s.toString())
            }

        }

        vm.observeIsCreateEnabled().observe(viewLifecycleOwner) { enabled ->
            binding.createPlaylistButton.isEnabled = enabled
        }

        binding.nameTextInputLayout.editText?.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        binding.nameTextInputLayout.editText?.removeTextChangedListener(textWatcher)
        textWatcher = null
        super.onDestroyView()
    }

    companion object {
        fun createArgs() =
            Bundle().apply {
            }
    }
}