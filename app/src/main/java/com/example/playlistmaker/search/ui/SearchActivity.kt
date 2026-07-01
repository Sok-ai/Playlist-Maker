package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.SearchResult
import com.example.playlistmaker.domain.model.Song
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.library.TRACK_ID_KEY
import com.example.playlistmaker.utils.Debounce

private typealias SearchDebounce = Debounce
private typealias ClickDebounce = Debounce

class SearchActivity : AppCompatActivity() {

    private val songInteractor = Creator.provideSongsInteractor()

    private lateinit var songAdapter: SongAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    private lateinit var searchInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var btnBack: ImageButton

    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var recyclerSearchHistory: RecyclerView
    private lateinit var clearSearchHistory: Button

    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var errorButton: Button

    private lateinit var recyclerTrack: RecyclerView
    private lateinit var progressBar: ProgressBar

    val searchDebounce = SearchDebounce(2000)
    val clickDebounce = ClickDebounce(500)

    private val songsList = arrayListOf<Song>()
    private var saveInputText = ""
    private var inputText = INPUT_TEXT_DEF

    private var debounceClickSong = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(top = statusBar.top, bottom = navBar.bottom)
            insets
        }
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        initializationViews()

        searchInput.setText(inputText)

        songAdapter = SongAdapter { song ->
            if (clickDebounce()) {
                songInteractor.addToHistory(song)
                openInformationAboutMusic(song.trackId)
                searchHistoryAdapter.searchHistoryList = songInteractor.getHistory()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }.apply {
            songs = songsList
        }

        recyclerTrack.adapter = songAdapter

        searchHistoryAdapter = SearchHistoryAdapter { song ->
            if (clickDebounce()) {
                openInformationAboutMusic(song.trackId)
            }
        }.apply {
            searchHistoryList = songInteractor.getHistory()
        }
        recyclerSearchHistory.adapter = searchHistoryAdapter

        btnBack.setOnClickListener {
            finish()
        }

        searchInput.setOnFocusChangeListener { _, hasFocus ->
            showSearchHistory(hasFocus)
        }

        clearButton.setOnClickListener {
            searchInput.setText("")
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()
            searchDebounce.cancel()
        }

        clearSearchHistory.setOnClickListener {
            songInteractor.clearHistory()
            searchHistoryAdapter.searchHistoryList = emptyList()
            searchHistoryAdapter.notifyDataSetChanged()
            showSearchHistory(false)
        }

        searchInput.doOnTextChanged { text, _, _, _ ->
            inputText = text.toString()
            clearButton.visibility = clearButtonVisibility(text)
            if (text?.isEmpty() == true) {
                clearSearchActivity()
            }
            searchDebounce.run {
                search(text.toString())
            }
            showSearchHistory(searchInput.hasFocus())
        }

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            searchInput.clearFocus()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchInput.text.isNotEmpty()) {
                    search(searchInput.text.toString())
                    true
                }
            }
            false
        }

        errorButton.setOnClickListener {
            search(saveInputText)
        }
    }

    private fun initializationViews() {
        btnBack = findViewById(R.id.btn_settings_to_main)
        clearButton = findViewById(R.id.clearIcon)
        searchInput = findViewById(R.id.inputEditText)
        recyclerTrack = findViewById(R.id.recyclerViewTrack)
        searchHistoryLayout = findViewById(R.id.searchHistoryLayout)
        recyclerSearchHistory = findViewById(R.id.recyclerSearchHistory)
        progressBar = findViewById(R.id.progressBarSongs)
        clearSearchHistory = findViewById(R.id.clearSearchHistory)
        errorImage = findViewById(R.id.placeholderError)
        errorMessage = findViewById(R.id.errorMessage)
        errorButton = findViewById(R.id.btnRefreshNetwork)
        errorLayout = findViewById(R.id.errorLayout)
    }

    private fun clearSearchActivity() {
        songAdapter.songs.clear()
        songAdapter.notifyDataSetChanged()
        recyclerTrack.visibility = VISIBLE
        errorLayout.visibility = GONE
        progressBar.visibility = GONE
        searchHistoryLayout.visibility = GONE
    }

    private fun clickDebounce(): Boolean {
        val current = debounceClickSong
        if (debounceClickSong) {
            debounceClickSong = false
            clickDebounce.run {
                debounceClickSong = true
            }
        }
        return current
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            VISIBLE
        }
    }

    private fun search(input: String) {
        showUiLoadingData()
        songInteractor.searchSongs(input) { result ->
            clearSearchActivity()
            when (result) {
                is SearchResult.Success -> {
                    if (result.songs.isNotEmpty()) {
                        songsList.addAll(result.songs)
                        songAdapter.notifyDataSetChanged()
                    } else {
                        showEmptyError()
                    }
                }

                is SearchResult.Empty -> showEmptyError()

                is SearchResult.Error -> {
                    saveInputText = searchInput.text.toString()
                    showNetworkError()
                }
            }
        }
    }

    private fun showEmptyError() {
        recyclerTrack.visibility = GONE
        errorButton.visibility = GONE
        searchHistoryLayout.visibility = GONE
        errorMessage.setText(R.string.search_empty)
        errorImage.setImageDrawable(getDrawable(R.drawable.ic_empty_song))
        errorLayout.visibility = VISIBLE
    }

    private fun showNetworkError() {
        recyclerTrack.visibility = GONE
        errorButton.visibility = VISIBLE
        searchHistoryLayout.visibility = GONE
        errorMessage.setText(R.string.network_error)
        errorImage.setImageDrawable(getDrawable(R.drawable.ic_network_error))
        errorLayout.visibility = VISIBLE
    }

    private fun showUiLoadingData() {
        progressBar.visibility = VISIBLE
        recyclerTrack.visibility = GONE
        searchHistoryLayout.visibility = GONE
        errorLayout.visibility = GONE
    }

    private fun showSearchHistory(hasFocus: Boolean) {
        val showHistory = hasFocus && searchInput.text.isEmpty()
        searchHistoryLayout.visibility =
            if (showHistory && searchHistoryAdapter.searchHistoryList.isNotEmpty()) VISIBLE else GONE
        if (showHistory) {
            recyclerTrack.visibility = GONE
            errorLayout.visibility = GONE
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

    override fun onStop() {
        super.onStop()
        clickDebounce.cancel()
        searchDebounce.cancel()
    }

    companion object {
        const val INPUT_TEXT_DEF = ""
        const val INPUT_TEXT_KEY = "INPUT_TEXT_KEY"
    }
}