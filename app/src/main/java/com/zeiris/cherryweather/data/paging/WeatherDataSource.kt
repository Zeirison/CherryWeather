package com.zeiris.cherryweather.data.paging

import android.annotation.SuppressLint
import androidx.paging.PageKeyedDataSource
import com.zeiris.cherryweather.data.db.dao.WeatherDao
import com.zeiris.cherryweather.data.model.Weather
import io.reactivex.schedulers.Schedulers

class WeatherDataSource(
    private val dao: WeatherDao
) : PageKeyedDataSource<Int, Weather>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Weather>
    ) {
        val limit = params.requestedLoadSize
        loadData(1, limit) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Weather>
    ) {
        val page = params.key
        val limit = params.requestedLoadSize
        loadData(page, limit) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Weather>
    ) {
        val page = params.key
        val limit = params.requestedLoadSize
        loadData(page, limit) {
            callback.onResult(it, page - 1)
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData(
        page: Int,
        limit: Int,
        callback: (List<Weather>) -> Unit
    ) {
        dao.getWeather(page, limit)
            .subscribeOn(Schedulers.io())
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            })
    }
}