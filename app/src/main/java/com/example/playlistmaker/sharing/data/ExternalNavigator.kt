package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.Navigator

class ExternalNavigator(private val context: Context) : Navigator {
    override fun shareLink() {
        val link = context.getString(R.string.course_link)
        val intentShare = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                link
            )
        }
        context.startActivity(intentShare)
    }

    override fun sendEmail() {
        val email = context.getString(R.string.student_email)
        val subject = context.getString(R.string.email_subject)
        val body = context.getString(R.string.email_text)
        val intentSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(
                Intent.EXTRA_SUBJECT,
                subject
            )
            putExtra(
                Intent.EXTRA_TEXT,
                body
            )
        }
        context.startActivity(intentSupport)
    }

    override fun openUrl() {
        val url = context.getString(R.string.course_user_agreement)
        val intentUserAgree =
            Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intentUserAgree)
    }

}