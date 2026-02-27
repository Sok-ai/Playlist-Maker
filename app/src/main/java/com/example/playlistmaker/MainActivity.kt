package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.btn_search)
        val library = findViewById<Button>(R.id.btn_library)
        val settings = findViewById<Button>(R.id.btn_settings)

        val clickOnSearch = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        search.setOnClickListener(clickOnSearch)

        library.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, LibraryActivity::class.java)
            startActivity(searchIntent)
        }

        settings.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(searchIntent)
        }
    }
}