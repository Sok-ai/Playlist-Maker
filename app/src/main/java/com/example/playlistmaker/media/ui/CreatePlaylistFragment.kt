package com.example.playlistmaker.media.ui

import android.net.Uri
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
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.core.ui.dialog.ConfirmationDialog
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

open class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {
    protected open val vm: CreatePlaylistViewModel by viewModel()
    private var lastLoadedCoverUri: Uri? = null
    private var lastLoadedExistingPath: String? = null
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
                lastLoadedCoverUri = it
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
        vm.observeResponseSavePlaylist().observe(viewLifecycleOwner) { name ->
            onSaveSuccess(name)
        }
        vm.observePlaylistState().observe(viewLifecycleOwner) { state ->
            binding.createPlaylistButton.isEnabled = state.isCreateEnabled

            val currentName = binding.nameTextInputLayout.editText?.text?.toString().orEmpty()
            if (currentName != state.name) {
                with(binding.nameTextInputLayout) {
                    editText?.setText(state.name)
                    editText?.setSelection(state.name.length)
                }
            }
            val currentDescription =
                binding.descriptionTextInputLayout.editText?.text?.toString().orEmpty()
            if (currentDescription != state.description) {
                with(binding.descriptionTextInputLayout) {
                    editText?.setText(state.description)
                    editText?.setSelection(state.description.length)
                }
            }
            val newCoverUri = state.coverUri
            val existingPath = state.existingCoverPath

            if (newCoverUri != null) {
                if (newCoverUri != lastLoadedCoverUri) {
                    binding.playlistImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(this)
                        .load(newCoverUri)
                        .transform(
                            RoundedCorners(
                                requireContext().dpToPx(8f)
                            )
                        )
                        .into(binding.playlistImage)
                    lastLoadedCoverUri = newCoverUri
                    lastLoadedExistingPath = null
                }
            } else if (existingPath != null) {
                if (existingPath != lastLoadedExistingPath) {
                    binding.playlistImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(this)
                        .load(File(existingPath))
                        .transform(
                            RoundedCorners(
                                requireContext().dpToPx(8f)
                            )
                        )
                        .into(binding.playlistImage)
                    lastLoadedExistingPath = existingPath
                    lastLoadedCoverUri = null
                }
            }
        }
        with(binding) {
            backButton.setOnClickListener {
                handleBack()
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
                vm.createPlaylist()
            }
        }
    }

    protected open fun onSaveSuccess(name: String) {
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(KEY_PLAYLIST_CREATED, name)
        findNavController().navigateUp()
    }

    private fun createDialog() {
        ConfirmationDialog.show(
            context = requireContext(),
            title = getString(R.string.dialog_exit_playlist_title),
            message = getString(R.string.dialog_exit_playlist_message),
            positiveText = getString(R.string.dialog_exit_positive),
            negativeText = getString(R.string.dialog_exit_negative),
            onPositive = {
                findNavController().navigateUp()
            },
            onNegative = {
            }
        )
    }

    protected open fun handleBack() {
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
                        handleBack()
                    }
                })
    }

    override fun onDestroyView() {
        lastLoadedCoverUri = null
        lastLoadedExistingPath = null
        super.onDestroyView()
    }

    companion object {
        const val KEY_PLAYLIST_CREATED = "playlist_created"
    }
}