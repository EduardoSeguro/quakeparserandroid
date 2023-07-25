package com.eduardoseguro.quakeparserapp

import android.app.Application
import com.eduardoseguro.quakeparserapp.di.quakeParserModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuakeParserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuakeParserApp)
            modules(quakeParserModule)
        }
    }
}