package com.zeiris.cherryweather.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zeiris.cherryweather.data.db.dao.WeatherDao
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.paging.WeatherDataSourceFactory
import com.zeiris.cherryweather.data.remote.api.WeatherApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

val weatherRepoModule = module {
    factory { WeatherRepository(get(), get(), get()) }
}

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val weatherApi: WeatherApi,
    private val weatherDataSource: WeatherDataSourceFactory
) {

    fun getWeather(): LiveData<PagedList<Weather>> {
        val result = weatherDataSource
        return LivePagedListBuilder(result, pagedListConfig()).build()
    }

    @SuppressLint("CheckResult")
    fun getWeatherByCityId(cityId: Int): Observable<Weather> {
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
        return weatherDao.getWeatherByCityId(cityId)
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(5)
        .setPageSize(5)
        .build()

}