package com.zeiris.cherryweather.data

import com.google.auto.service.AutoService
import com.zeiris.cherryweather.data.db.databaseModule
import com.zeiris.cherryweather.data.paging.weatherDataSourceModule
import com.zeiris.cherryweather.data.remote.network.networkModule
import com.zeiris.cherryweather.data.repository.weatherRepoModule
import com.zeiris.cherryweather.utils.KoinModule

@AutoService(KoinModule::class)
class DataKoinModule : KoinModule {
    override val koinModulesList = listOf(
        networkModule,
        databaseModule,
        weatherRepoModule,
        weatherDataSourceModule
    )
}