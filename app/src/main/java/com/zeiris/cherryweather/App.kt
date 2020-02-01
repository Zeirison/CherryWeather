package com.zeiris.cherryweather

import android.app.Application
import com.zeiris.cherryweather.utils.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*

class App : Application() {

    private val koinModules =
        ServiceLoader.load(KoinModule::class.java).flatMap { it.koinModulesList }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            androidFileProperties()
            modules(koinModules)
        }
    }
}