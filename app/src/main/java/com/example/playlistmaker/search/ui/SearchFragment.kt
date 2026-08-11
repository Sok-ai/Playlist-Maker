package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.core.BindingFragment
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.library.ui.activity.LibraryFragment
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<ActivitySearchBinding>() {
    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    private lateinit var songAdapter: SongAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var saveInputText = ""
    private var inputText = INPUT_TEXT_DEF

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivitySearchBinding = ActivitySearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.inputEditText.setText(inputText)

        viewModel.observeStateNetwork().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeNavigationToPlayer().observe(viewLifecycleOwner) {
            openInformationAboutMusic(it)
        }

        songAdapter = SongAdapter { song ->
            viewModel.onSongClicked(song)
        }

        viewModel.observeSongsList().observe(viewLifecycleOwner) {
            songAdapter.songs = it
        }

        binding.recyclerViewTrack.adapter = songAdapter

        searchHistoryAdapter = SearchHistoryAdapter { song ->
            viewModel.onSongHistoryClicked(song)
        }

        viewModel.observeHistory().observe(viewLifecycleOwner) {
            searchHistoryAdapter.searchHistoryList = it
        }

        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            showSearchHistory(hasFocus)
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
            binding.inputEditText.clearFocus()
            viewModel.clearSongsList()
            viewModel.cancelRequest()
        }

        binding.clearSearchHistory.setOnClickListener {
            viewModel.clearHistory()
            showSearchHistory(false)
        }

        binding.inputEditText.doOnTextChanged { text, _, _, _ ->
            inputText = text.toString()
            binding.clearIcon.visibility = clearButtonVisibility(text)
            if (text?.isEmpty() == true) {
                viewModel.clearSongsList()
            }
            viewModel.requestToNetwork(text.toString())
            showSearchHistory(binding.inputEditText.hasFocus())
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            binding.inputEditText.clearFocus()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    viewModel.searchSongs(binding.inputEditText.text.toString())
                    true
                }
            }
            false
        }

        binding.btnRefreshNetwork.setOnClickListener {
            viewModel.repeatRequest()
        }
    }

    fun render(state: SearchResult) {
        when (state) {
            SearchResult.Empty -> {
                showEmptyError()
            }

            is SearchResult.Error -> {
                showNetworkError()
            }

            is SearchResult.Success -> {
                showSuccess()
            }

            SearchResult.Loading -> showUiLoadingData()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showSuccess() {
        with(binding) {
            progressBarSongs.visibility = View.GONE
            recyclerViewTrack.visibility = View.VISIBLE
            searchHistoryLayout.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
    }

    private fun showEmptyError() {
        with(binding) {
            progressBarSongs.visibility = View.GONE
            recyclerViewTrack.visibility = View.GONE
            btnRefreshNetwork.visibility = View.GONE
            searchHistoryLayout.visibility = View.GONE
            errorMessage.setText(R.string.search_empty)
            placeholderError.setImageDrawable(requireContext().getDrawable(R.drawable.ic_empty_song))
            errorLayout.visibility = View.VISIBLE
        }
    }

    private fun showNetworkError() {
        with(binding) {
            progressBarSongs.visibility = View.GONE
            recyclerViewTrack.visibility = View.GONE
            btnRefreshNetwork.visibility = View.VISIBLE
            searchHistoryLayout.visibility = View.GONE
            errorMessage.setText(R.string.network_error)
            placeholderError.setImageDrawable(requireContext().getDrawable(R.drawable.ic_network_error))
            errorLayout.visibility = View.VISIBLE
        }
    }

    private fun showUiLoadingData() {
        with(binding) {
            progressBarSongs.visibility = View.VISIBLE
            recyclerViewTrack.visibility = View.GONE
            searchHistoryLayout.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
    }

    private fun showSearchHistory(hasFocus: Boolean) {
        val showHistory = hasFocus && binding.inputEditText.text.isEmpty()
        binding.searchHistoryLayout.visibility =
            if (showHistory && searchHistoryAdapter.searchHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        if (showHistory) {
            binding.recyclerViewTrack.visibility = View.GONE
            binding.errorLayout.visibility = View.GONE
        }
    }

    private fun openInformationAboutMusic(songId: Long) {
        findNavController().navigate(
            R.id.action_searchFragment_to_libraryFragment,
            LibraryFragment.createArgs(songId)
        )
    }

    override fun onSaveInstanceState(
        outState: Bundle
    ) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT_KEY, inputText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            inputText = savedInstanceState.getString(INPUT_TEXT_KEY, INPUT_TEXT_DEF)
        }
    }

    companion object {
        const val INPUT_TEXT_DEF = ""
        const val INPUT_TEXT_KEY = "INPUT_TEXT_KEY"
    }
}