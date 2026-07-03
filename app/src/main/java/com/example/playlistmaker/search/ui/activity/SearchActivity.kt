package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.ui.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.SongAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.library.ui.activity.LibraryActivity
import com.example.playlistmaker.library.ui.activity.TRACK_ID_KEY

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel

    private lateinit var songAdapter: SongAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var saveInputText = ""
    private var inputText = INPUT_TEXT_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
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

        viewModel =
            ViewModelProvider(
                this,
                SearchViewModel.getFactory(Creator.provideSearchInteractor())
            )[SearchViewModel::class.java]

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.inputEditText.setText(inputText)

        viewModel.observeStateNetwork().observe(this) {
            render(it)
        }

        viewModel.observeNavigationToPlayer().observe(this) {
            openInformationAboutMusic(it)
        }

        songAdapter = SongAdapter { song ->
            viewModel.onSongClicked(song)
        }

        viewModel.observeSongsList().observe(this) {
            songAdapter.songs = it
        }

        binding.recyclerViewTrack.adapter = songAdapter

        searchHistoryAdapter = SearchHistoryAdapter { song ->
            viewModel.onSongHistoryClicked(song)
        }

        viewModel.observeHistory().observe(this) {
            searchHistoryAdapter.searchHistoryList = it
        }

        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        binding.btnSettingsToMain.setOnClickListener {
            finish()
        }

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
            placeholderError.setImageDrawable(getDrawable(R.drawable.ic_empty_song))
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
            placeholderError.setImageDrawable(getDrawable(R.drawable.ic_network_error))
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
        val intent = Intent(this, LibraryActivity::class.java).apply {
            putExtra(TRACK_ID_KEY, songId)
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(
        outState: Bundle
    ) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT_KEY, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(INPUT_TEXT_KEY, INPUT_TEXT_DEF)
    }

    companion object {
        const val INPUT_TEXT_DEF = ""
        const val INPUT_TEXT_KEY = "INPUT_TEXT_KEY"
    }
}