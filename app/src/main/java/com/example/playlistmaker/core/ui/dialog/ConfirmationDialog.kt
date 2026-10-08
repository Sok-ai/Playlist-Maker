package com.example.playlistmaker.core.ui.dialog

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ConfirmationDialog {
    fun show(
        context: Context,
        title: String,
        message: String,
        positiveText: String = "Да",
        negativeText: String = "Нет",
        onPositive: () -> Unit,
        onNegative: () -> Unit,
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, { dialog, switch ->
                onPositive.invoke()
            })
            .setNegativeButton(negativeText, { dialog, switch ->
                onNegative.invoke()
            })
            .show()
    }
}