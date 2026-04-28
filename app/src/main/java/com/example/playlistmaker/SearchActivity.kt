package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private val songApi = RetrofitClient.trackService

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

    private val songsList = arrayListOf<Song>()
    private var saveInputText = ""
    private var inputText = INPUT_TEXT_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPref)

        initializationViews()

        searchInput.setText(inputText)

        songAdapter = SongAdapter { song ->
            searchHistory.putSongs(song)
            searchHistoryAdapter.searchHistoryList = searchHistory.getSongs()
            searchHistoryAdapter.notifyDataSetChanged()
        }.apply {
            songs = songsList
        }

        recyclerTrack.adapter = songAdapter

        searchHistoryAdapter = SearchHistoryAdapter().apply {
            searchHistoryList = searchHistory.getSongs()
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
        }

        clearSearchHistory.setOnClickListener {
            searchHistory.clearSongsFromShared()
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
        clearSearchHistory = findViewById(R.id.clearSearchHistory)
        errorImage = findViewById(R.id.placeholderError)
        errorMessage = findViewById(R.id.errorMessage)
        errorButton = findViewById(R.id.btnRefreshNetwork)
        errorLayout = findViewById(R.id.errorLayout)
    }

    private fun clearSearchActivity() {
        songAdapter.songs.clear()
        songAdapter.notifyDataSetChanged()
        recyclerTrack.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(input: String) {
        songApi.getSongs(input).enqueue(object : Callback<SongsResponse> {
            override fun onResponse(
                call: Call<SongsResponse?>, response: Response<SongsResponse?>
            ) {
                clearSearchActivity()
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            songsList.addAll(response.body()?.results!!)
                            songAdapter.notifyDataSetChanged()
                        } else {
                            showEmptyError()
                        }
                    }

                    else -> {
                        saveInputText = searchInput.text.toString()
                        showNetworkError()
                    }
                }
            }

            override fun onFailure(
                call: Call<SongsResponse?>, t: Throwable
            ) {
                saveInputText = searchInput.text.toString()
                showNetworkError()
                t.stackTrace
            }

        })
    }

    private fun showEmptyError() {
        recyclerTrack.visibility = View.GONE
        errorButton.visibility = View.GONE
        errorMessage.setText(R.string.search_empty)
        errorImage.setImageDrawable(getDrawable(R.drawable.ic_empty_song))
        errorLayout.visibility = View.VISIBLE
    }

    private fun showNetworkError() {
        recyclerTrack.visibility = View.GONE
        errorButton.visibility = View.VISIBLE
        errorMessage.setText(R.string.network_error)
        errorImage.setImageDrawable(getDrawable(R.drawable.ic_network_error))
        errorLayout.visibility = View.VISIBLE
    }

    private fun showSearchHistory(hasFocus: Boolean) {
        val checkField = hasFocus && searchInput.text.isEmpty()
        searchHistoryLayout.visibility =
            if (checkField && searchHistoryAdapter.searchHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        recyclerTrack.visibility = if (!checkField) View.VISIBLE else View.GONE
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