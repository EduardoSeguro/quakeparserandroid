package com.eduardoseguro.quakeparserapp.ui.components

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eduardoseguro.quakeparserapp.R

@Preview(showBackground = true)
@Composable
fun LoadLogButton(loadLog: () -> Unit = {}) {
    OutlinedButton(onClick = loadLog) {
        Text(stringResource(id = R.string.load_log_button))
    }
}