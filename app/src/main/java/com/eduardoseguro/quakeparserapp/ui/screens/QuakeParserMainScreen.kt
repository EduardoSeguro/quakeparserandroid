package com.eduardoseguro.quakeparserapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eduardoseguro.quakeparserapp.ui.components.AlertDialogParseError
import com.eduardoseguro.quakeparserapp.ui.components.GameReportItem
import com.eduardoseguro.quakeparserapp.ui.components.LoadLogButton
import com.eduardoseguro.quakeparserapp.ui.components.QuakeTitle
import com.eduardoseguro.quakeparserapp.viewmodel.QuakeParserViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun QuakeParserScreen(
    viewModel: QuakeParserViewModel = koinViewModel()
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        val parserState = viewModel.parserState.observeAsState()
        val data = remember {
            mutableStateOf<Uri?>(null)
        }
        val openFileLauncher: ActivityResultLauncher<Intent> = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            data.value = result.data?.data
            viewModel.parseLogFile(data.value)
        }

        Column(
            modifier = Modifier
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val showDialog = remember { mutableStateOf(false) }

            QuakeTitle()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth(0.95f)
            ) {
                when (parserState.value?.first) {
                    QuakeParserViewModel.ParserState.LOADING -> CircularProgressIndicator()
                    QuakeParserViewModel.ParserState.COMPLETE -> {
                        Column(
                            Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            viewModel.gameReportList.forEach { gameReport ->
                                GameReportItem(gameReport)
                            }
                        }
                    }

                    else -> {}
                }
            }
            LoadLogButton(loadLog = {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                openFileLauncher.launch(intent)
            })
            if (showDialog.value) {
                AlertDialogParseError(
                    onConfirmButton = { viewModel.resetParserState() },
                    setShowDialog = { showDialog.value = it },
                    message = parserState.value?.second ?: ""
                )
            }
            when (parserState.value?.first) {
                QuakeParserViewModel.ParserState.ERROR,
                QuakeParserViewModel.ParserState.NO_DATA,
                QuakeParserViewModel.ParserState.INVALID_FILE -> {
                    showDialog.value = true
                }

                else -> showDialog.value = false
            }
        }
    }
}

@Preview
@Composable
fun QuakeParserScreenPreview() {
    QuakeParserScreen()
}