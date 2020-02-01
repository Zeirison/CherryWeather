package com.zeiris.cherryweather.ui.search

import androidx.lifecycle.ViewModel
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.repository.WeatherRepository
import io.reactivex.Observable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel { SearchViewModel(get()) }
}

class SearchViewModel(private val repo: WeatherRepository) : ViewModel() {

    val weather by lazy { repo.getWeather() }

    fun getWeatherByCityId(cityId: Int): Observable<Weather> {
        return repo.getWeatherByCityId(cityId)
    }

}