package com.ad.brainshopchatbot.util

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    dismissButton: Boolean = false,
    icon: ImageVector? = null,
) {
    AlertDialog(icon = {
        if (icon != null) {
            Icon(icon, contentDescription = "Example Icon")
        }
    }, title = {
        Text(text = dialogTitle)
    }, text = {
        Text(text = dialogText)
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text("Ok")
        }
    }, dismissButton = {
        if (dismissButton) {
            TextButton(onClick = {
                onDismissRequest()
            }) {
                Text("Dismiss")
            }
        } else null
    })
}