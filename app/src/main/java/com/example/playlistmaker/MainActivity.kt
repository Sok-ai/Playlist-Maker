package com.example.playlistmaker

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
                Toast.makeText(this@MainActivity, "Клик по поиску", Toast.LENGTH_SHORT).show()
            }
        }
        search.setOnClickListener(clickOnSearch)

        library.setOnClickListener {
            Toast.makeText(this@MainActivity, "Клик по медиатеке", Toast.LENGTH_SHORT).show()
        }

        settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Клик по настройкам", Toast.LENGTH_SHORT).show()
        }
    }
}