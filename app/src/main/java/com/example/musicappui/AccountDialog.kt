package com.example.musicappui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountDialog(dialogOpen: MutableState<Boolean>) {
    if (dialogOpen.value) {
        AlertDialog(onDismissRequest = {
            dialogOpen.value = false
        },
             confirmButton = {
                TextButton(onClick = { dialogOpen.value = false }) {
                    Text(text = "Confirm")
                }
            }, dismissButton = {
                TextButton(onClick = { dialogOpen.value = false }) {
                    Text(text = "Dismiss")
                }
            }, title = {
                Text(text = "Add Account")
            },
            text = {
                Column(modifier = Modifier.wrapContentHeight()) {
                    Text(text = "Add Account")
                }
            }

        )

    }
}