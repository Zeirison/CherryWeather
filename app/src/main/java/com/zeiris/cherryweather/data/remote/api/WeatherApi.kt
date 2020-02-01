package com.zeiris.cherryweather.data.remote.api

import com.zeiris.cherryweather.data.model.Weather
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    fun getWeatherByCityId(
        @Query("id") id: Int
    ): Observable<Response<Weather>>

    @GET("forecast")
    fun getWeatherByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Observable<Response<Weather>>

}