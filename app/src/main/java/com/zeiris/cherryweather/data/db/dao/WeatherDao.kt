package com.zeiris.cherryweather.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.zeiris.cherryweather.data.db.BaseDao
import com.zeiris.cherryweather.data.model.Weather
import io.reactivex.Observable

@Dao
interface WeatherDao : BaseDao<Weather> {

    @Query("SELECT * FROM weather LIMIT :limit OFFSET :limit * (:page - 1)")
    fun getWeather(page: Int, limit: Int): Observable<List<Weather>>

    @Query("SELECT * FROM weather WHERE id = :id")
    fun getWeatherByCityId(id: Int): Observable<Weather>

    @Query("SELECT * FROM weather WHERE name LIKE :name")
    fun getWeatherByCityName(name: String): Observable<Weather>

}