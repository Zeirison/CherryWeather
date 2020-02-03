package com.zeiris.cherryweather.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel { SearchViewModel(get()) }
}

class SearchViewModel(private val repo: WeatherRepository) : ViewModel() {

    val weather: MutableLiveData<List<Weather>> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()

    fun deleteWeather(list: List<Weather>): Completable {
        return repo.deleteWeather(list)
    }

    fun fetchSavedCities() {
        compositeDisposable.add(
            repo.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    weather.postValue(it)
                }
        )
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
        fetchWeatherByCityId(1851632) // Shuzenji
        fetchWeatherByCityId(2260494) // Republic of the Congo
    }

}