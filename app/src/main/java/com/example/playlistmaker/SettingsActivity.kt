package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var share: LinearLayout
    private lateinit var support: LinearLayout
    private lateinit var userAgree: LinearLayout
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        btnBack = findViewById(R.id.btn_settings_to_main)
        share = findViewById(R.id.setting_share)
        support = findViewById(R.id.setting_support)
        userAgree = findViewById(R.id.setting_user_agreement)
        themeSwitcher = findViewById(R.id.themeSwitcher)

        themeSwitcher.isChecked = sharedPref.getBoolean(THEME_KEY, false)

        share.setOnClickListener {
            val intentShare = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.course_link)
                )
            }
            startActivity(intentShare)
        }

        support.setOnClickListener {
            val intentSupport = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.email_subject)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.email_text)
                )
            }
            startActivity(intentSupport)
        }

        userAgree.setOnClickListener {
            val intentUserAgree =
                Intent(Intent.ACTION_VIEW, getString(R.string.course_user_agreement).toUri())
            startActivity(intentUserAgree)
        }

        btnBack.setOnClickListener {
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}