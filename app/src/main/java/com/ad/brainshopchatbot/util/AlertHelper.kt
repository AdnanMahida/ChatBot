package com.ad.brainshopchatbot.util

import android.content.Context
import android.content.DialogInterface
import com.ad.brainshopchatbot.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showAlertDialog(
    message: String,
    setPositiveButton: (DialogInterface) -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.app_name))
        .setMessage(message)
        .setPositiveButton("Ok") { dialog, _ ->
            setPositiveButton(dialog)
        }
        .show()
}
