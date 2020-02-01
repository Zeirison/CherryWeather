package com.zeiris.cherryweather.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.zeiris.cherryweather.data.model.Weather
import org.koin.dsl.module

val weatherDataSourceModule = module {
    factory { WeatherDataSource(get()) }
    factory { WeatherDataSourceFactory(get()) }
}

class WeatherDataSourceFactory(private val source: WeatherDataSource) :
    DataSource.Factory<Int, Weather>() {

    val liveData = MutableLiveData<WeatherDataSource>()

    override fun create(): DataSource<Int, Weather> {
        liveData.postValue(source)
        return source
    }

}