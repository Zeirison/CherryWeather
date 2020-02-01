package com.zeiris.cherryweather.ui

import com.google.auto.service.AutoService
import com.zeiris.cherryweather.ui.search.searchViewModelModule
import com.zeiris.cherryweather.utils.KoinModule

@AutoService(KoinModule::class)
class UiKoinModule : KoinModule {
    override val koinModulesList = listOf(
        searchViewModelModule
    )
}