package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {

    private val photoResultLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                Glide.with(this)
                    .load(it)
                    .transform(
                        RoundedCorners(
                            requireContext().dpToPx(8f)
                        )
                    )
                    .into(binding.playlistImage)
                binding.playlistImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    private val vm: CreatePlaylistViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreatePlaylistBinding =
        FragmentCreatePlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressedHandler()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                0,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        vm.observeIsCreateEnabled().observe(viewLifecycleOwner) { enabled ->
            binding.createPlaylistButton.isEnabled = enabled
        }
        binding.playlistImage.setOnClickListener {
            photoResultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.nameTextInputLayout.editText?.doAfterTextChanged { text ->
            vm.onNameChange(text.toString())
        }
        with(binding) {
            backButton.setOnClickListener {
                hasUnsavedData()
            }





    private fun onBackPressedHandler() {
        requireActivity().onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        hasUnsavedData()
                    }
                })
    }

    companion object {
        fun createArgs() =
            Bundle().apply {
            }
    }
}