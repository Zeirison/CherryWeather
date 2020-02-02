package com.zeiris.cherryweather.ui.search

import androidx.lifecycle.ViewModel
import com.zeiris.cherryweather.data.repository.WeatherRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel { SearchViewModel(get()) }
}

class SearchViewModel(private val repo: WeatherRepository) : ViewModel() {

    val weather by lazy { repo.getWeather() }

    fun fetchWeatherByCityId(cityId: Int) {
        repo.fetchWeatherByCityId(cityId)
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        repo.fetchWeatherByCoordinates(lat, lon)
    }

}