package com.eduardoseguro.quakeparserapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eduardoseguro.quakeparserapp.R

@Preview(showBackground = true)
@Composable
fun QuakeTitle() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.quake_icon),
            contentDescription = "Main Icon",
            modifier = Modifier
                .size(100.dp)
                .align(CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Text(
            stringResource(id = R.string.title),
            modifier = Modifier.align(CenterHorizontally)
        )
    }
}