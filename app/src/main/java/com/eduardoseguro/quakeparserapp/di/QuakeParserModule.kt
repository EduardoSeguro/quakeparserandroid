package com.eduardoseguro.quakeparserapp.di

import com.eduardoseguro.quakeparserapp.usecase.ParseLogFileUseCase
import com.eduardoseguro.quakeparserapp.viewmodel.QuakeParserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val quakeParserModule = module {

    single { androidContext().contentResolver }
    single { ParseLogFileUseCase(contentResolver = get()) }
    viewModel {
        QuakeParserViewModel(parseLogFileUseCase = get())
    }
}