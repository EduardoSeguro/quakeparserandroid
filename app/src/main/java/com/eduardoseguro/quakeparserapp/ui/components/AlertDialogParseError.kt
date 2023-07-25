package com.eduardoseguro.quakeparserapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eduardoseguro.quakeparserapp.R

@Preview(showBackground = true)
@Composable
fun AlertDialogParseError(
    onConfirmButton: () -> Unit = {},
    setShowDialog: (Boolean) -> Unit = {},
    message: String = ""
) {
    AlertDialog(
        onDismissRequest = {
            onConfirmButton()
            setShowDialog(false)
        },
        title = {
            Text(text = stringResource(id = R.string.invalid_log_file_title))
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmButton()
                    setShowDialog(false)
                }) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}