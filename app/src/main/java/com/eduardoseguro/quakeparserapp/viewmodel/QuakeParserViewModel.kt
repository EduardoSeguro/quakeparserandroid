package com.eduardoseguro.quakeparserapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoseguro.quakeparserapp.model.GameReport
import com.eduardoseguro.quakeparserapp.usecase.ParseLogFileUseCase
import com.eduardoseguro.quakeparserapp.utils.Constants
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class QuakeParserViewModel(
    private val parseLogFileUseCase: ParseLogFileUseCase
) : ViewModel() {

    private val _parserState = MutableLiveData<Pair<ParserState, String>>()
    val parserState: LiveData<Pair<ParserState, String>>
        get() = _parserState

    val gameReportList = mutableListOf<GameReport>()

    fun noFileSelected() {
        _parserState.postValue(Pair(ParserState.NO_DATA, Constants.NO_FILE_SELECTED))
    }

    fun invalidLogFile() {
        _parserState.postValue(Pair(ParserState.INVALID_FILE, Constants.INVALID_LOG_FILE))
    }

    fun parseLogFile(file: Uri?) {
        resetParserState()
        viewModelScope.launch(IO) {
            gameReportList.clear()
            _parserState.postValue(Pair(ParserState.LOADING, ""))
            val result = parseLogFileUseCase.loadLog(file)
            if (result.size == 1) {
                when (result[0]) {
                    Constants.INVALID_LOG_FILE -> invalidLogFile()
                    Constants.NO_FILE_SELECTED -> noFileSelected()
                    else -> _parserState.postValue(Pair(ParserState.ERROR, "Error loading log file"))
                }
            } else {
                val reports = parseLogFileUseCase.parseLogData(result)
                gameReportList.addAll(reports)
                _parserState.postValue(Pair(ParserState.COMPLETE, ""))
            }
        }
    }

    fun resetParserState() {
        _parserState.postValue(Pair(ParserState.INITIALIZING, ""))
    }

    enum class ParserState {
        INITIALIZING,
        LOADING,
        NO_DATA,
        INVALID_FILE,
        ERROR,
        COMPLETE
    }
}