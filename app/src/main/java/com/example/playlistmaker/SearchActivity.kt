package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {
    private var inputText = INPUT_TEXT_DEF

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBack = findViewById<ImageButton>(R.id.btn_settings_to_main)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val searchInput = findViewById<EditText>(R.id.inputEditText)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

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
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val INPUT_TEXT_DEF = ""
        const val INPUT_TEXT_KEY = "INPUT_TEXT_KEY"
    }
}