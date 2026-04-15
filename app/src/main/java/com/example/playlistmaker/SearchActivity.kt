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

    private val adapter = SongAdapter()

    private lateinit var searchInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var btnBack: ImageButton

    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var errorButton: Button

    private lateinit var recyclerTask: RecyclerView

    private val songsList = arrayListOf<Song>()
    private var saveInputText = ""
    private var inputText = INPUT_TEXT_DEF

    override fun onSaveInstanceState(
        outState: Bundle
    ) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT_KEY, inputText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }


        btnBack = findViewById(R.id.btn_settings_to_main)
        clearButton = findViewById(R.id.clearIcon)
        searchInput = findViewById(R.id.inputEditText)
        recyclerTask = findViewById(R.id.recyclerViewTrack)

        errorImage = findViewById(R.id.placeholderError)
        errorMessage = findViewById(R.id.errorMessage)
        errorButton = findViewById(R.id.btnRefreshNetwork)
        errorLayout = findViewById(R.id.errorLayout)

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        adapter.songs = songsList

        recyclerTask.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        searchInput.setText(inputText)

        clearButton.setOnClickListener {
            searchInput.setText("")
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()
        }

        searchInput.doOnTextChanged { text, _, _, _ ->
            inputText = text.toString()
            clearButton.visibility = clearButtonVisibility(text)
            if (text?.isEmpty() == true) {
                clearSearchActivity()
            }
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(INPUT_TEXT_KEY, INPUT_TEXT_DEF)
    }

    private fun clearSearchActivity() {
        adapter.songs.clear()
        adapter.notifyDataSetChanged()
        recyclerTask.visibility = View.VISIBLE
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
                            adapter.notifyDataSetChanged()
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
        recyclerTask.visibility = View.GONE
        errorButton.visibility = View.GONE
        errorMessage.setText(R.string.search_empty)
        errorImage.setImageDrawable(getDrawable(R.drawable.ic_empty_song))
        errorLayout.visibility = View.VISIBLE
    }

    private fun showNetworkError() {
        recyclerTask.visibility = View.GONE
        errorButton.visibility = View.VISIBLE
        errorMessage.setText(R.string.network_error)
        errorImage.setImageDrawable(getDrawable(R.drawable.ic_network_error))
        errorLayout.visibility = View.VISIBLE
    }

    companion object {
        const val INPUT_TEXT_DEF = ""
        const val INPUT_TEXT_KEY = "INPUT_TEXT_KEY"
    }
}