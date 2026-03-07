package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<ImageButton>(R.id.btn_settings_to_main)
        val share = findViewById<LinearLayout>(R.id.setting_share)
        val support = findViewById<LinearLayout>(R.id.setting_support)
        val userAgree = findViewById<LinearLayout>(R.id.setting_user_agreement)

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://practicum.yandex.ru/profile/android-developer-plus/"
                )
            }
            startActivity(intent)
        }

        support.setOnClickListener {

        }

        userAgree.setOnClickListener {

        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}