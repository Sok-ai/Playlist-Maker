package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(top = statusBar.top, bottom = navBar.bottom)
            insets
        }

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