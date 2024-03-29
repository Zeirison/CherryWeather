package com.zeiris.cherryweather.data.repository

import android.annotation.SuppressLint
import com.zeiris.cherryweather.data.db.dao.WeatherDao
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.remote.api.WeatherApi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

val weatherRepoModule = module {
    single { WeatherRepository(get(), get()) }
}

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val weatherApi: WeatherApi
) {

    fun getWeather(): Observable<List<Weather>> {
        return weatherDao.getWeather()
    }

    fun deleteWeather(list: List<Weather>): Completable {
        return Completable.fromAction { weatherDao.delete(list) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun fetchWeatherByCityName(name: String) {
        weatherApi.getWeatherByCityName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    response.body()?.let { weather ->
                        Observable.just(weatherDao)
                            .subscribeOn(Schedulers.io())
                            .subscribe { db ->
                                db.insert(weather)
                            }
                    }
                }
            }, { e ->
                e.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    fun fetchWeatherByCityId(cityId: Int) {
        weatherApi.getWeatherByCityId(cityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    response.body()?.let { weather ->
                        Observable.just(weatherDao)
                            .subscribeOn(Schedulers.io())
                            .subscribe { db ->
                                db.insert(weather)
                            }
                    }
                }
            }, { e ->
                e.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        weatherApi.getWeatherByCoordinates(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    response.body()?.let { weather ->
                        Observable.just(weatherDao)
                            .subscribeOn(Schedulers.io())
                            .subscribe { db ->
                                db.insert(weather)
                            }
                    }
                }

            }, { e ->
                e.printStackTrace()
            })
    }

}