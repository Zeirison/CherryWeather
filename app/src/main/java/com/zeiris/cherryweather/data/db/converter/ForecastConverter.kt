package com.zeiris.cherryweather.data.db.converter

import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.readValue
import com.zeiris.cherryweather.data.model.Forecast
import com.zeiris.cherryweather.data.remote.network.provideObjectMapper

object ForecastConverter {

    private var mapper = provideObjectMapper()

    @TypeConverter
    @JvmStatic
    fun fromJsonToArray(value: String): List<Forecast> {
        return mapper.readValue(value)
    }

    @TypeConverter
    @JvmStatic
    fun arrayToJson(forecast: List<Forecast>): String {
        return mapper.writeValueAsString(forecast)
    }


    @TypeConverter
    @JvmStatic
    fun fromJson(value: String): Forecast {
        return mapper.readValue(value)
    }

    @TypeConverter
    @JvmStatic
    fun toJson(forecast: Forecast): String {
        return mapper.writeValueAsString(forecast)
    }

}
