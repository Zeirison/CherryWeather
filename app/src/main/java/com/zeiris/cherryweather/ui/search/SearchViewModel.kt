package com.zeiris.cherryweather.ui.search

import androidx.lifecycle.ViewModel
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.repository.WeatherRepository
import io.reactivex.Completable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel { SearchViewModel(get()) }
}

class SearchViewModel(private val repo: WeatherRepository) : ViewModel() {

    val weather by lazy { repo.getWeather() }

    fun deleteWeather(list: List<Weather>): Completable {
        return repo.deleteWeather(list)
    }

    fun fetchWeatherByCityName(name: String) {
        repo.fetchWeatherByCityName(name)
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        repo.fetchWeatherByCoordinates(lat, lon)
    }

    private fun fetchWeatherByCityId(cityId: Int) {
        repo.fetchWeatherByCityId(cityId)
    }

    fun fetchDummyCities() {
        fetchWeatherByCityId(1851632)
        fetchWeatherByCityId(709930)
    }

}