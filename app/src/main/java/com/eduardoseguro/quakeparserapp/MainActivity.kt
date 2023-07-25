package com.eduardoseguro.quakeparserapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eduardoseguro.quakeparserapp.ui.screens.QuakeParserScreen
import com.eduardoseguro.quakeparserapp.ui.theme.QuakeParserAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuakeParserAppTheme {
                QuakeParserScreen()
            }
        }
    }
}