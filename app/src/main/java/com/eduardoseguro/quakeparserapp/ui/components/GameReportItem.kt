package com.eduardoseguro.quakeparserapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eduardoseguro.quakeparserapp.model.GameReport
import com.eduardoseguro.quakeparserapp.model.gameReportPreview

@Preview
@Composable
fun GameReportItem(gameReport: GameReport = gameReportPreview) {
    Card(modifier = Modifier.fillMaxWidth()) {
        val expanded = remember { mutableStateOf(false) }
        Column(modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable { expanded.value = !expanded.value }
            .fillMaxWidth()) {
            Text(text = "Game ${gameReport.id}")
            if (expanded.value) {
                Divider()
                if (gameReport.invalidGame) {
                    Text(text = "Invalid game")
                    Text(text = "Reasons: ${gameReport.invalidReasons}")
                } else {
                    Text(text = "Total kills: ${gameReport.totalKills}")
                    Text(text = "Players: ${gameReport.players}")
                    Text(text = "Kills: ${gameReport.kills}")
                    Text(text = "Kills by means: ${gameReport.killsByMeans}")
                }
            }
        }
    }
}