package com.zeiris.cherryweather.di

import com.google.auto.service.AutoService
import com.zeiris.cherryweather.ui.search.searchViewModelModule
import com.zeiris.cherryweather.utils.KoinModule

@AutoService(KoinModule::class)
class UiKoinModule : KoinModule {
    override val koinModulesList = listOf(
        searchViewModelModule
    )
}