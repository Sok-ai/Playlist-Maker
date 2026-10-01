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
import com.example.playlistmaker.core.ui.dialog.ConfirmationDialog
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {
    private val vm: CreatePlaylistViewModel by viewModel()
    private val photoResultLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                binding.playlistImage.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this)
                    .load(it)
                    .transform(
                        RoundedCorners(
                            requireContext().dpToPx(8f)
                        )
                    )
                    .into(binding.playlistImage)
                vm.onImageChange(it)
            }
        }

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
        vm.observePlaylistState().observe(viewLifecycleOwner) { state ->
            binding.createPlaylistButton.isEnabled = state.isCreateEnabled
        }
        with(binding) {
            backButton.setOnClickListener {
                hasUnsavedData()
            }
            playlistImage.setOnClickListener {
                photoResultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            nameTextInputLayout.editText?.doAfterTextChanged { text ->
                vm.onNameChange(text.toString())
            }
            descriptionTextInputLayout.editText?.doAfterTextChanged { text ->
                vm.onDescriptionChange(text.toString())
            }
            createPlaylistButton.setOnClickListener {
                onPlaylistCreated(vm.getPlaylistName())
            }
        }
    }

    private fun onPlaylistCreated(playlistName: String) {
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(KEY_PLAYLIST_CREATED, playlistName)

        findNavController().navigateUp()
    }

    private fun createDialog() {
        ConfirmationDialog.show(
            context = requireContext(),
            title = "Завершить создание плейлиста?",
            message = "Все несохраненные данные будут потеряны",
            positiveText = "Завершить",
            negativeText = "Отмена",
            onPositive = {
                findNavController().navigateUp()
            },
            onNegative = {
            }
        )
    }

    private fun hasUnsavedData() {
        if (vm.hasUnsavedData()) {
            createDialog()
        } else {
            findNavController().navigateUp()
        }
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
        const val KEY_PLAYLIST_CREATED = "playlist_created"
    }
}