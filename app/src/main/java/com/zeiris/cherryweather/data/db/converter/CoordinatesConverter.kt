package com.zeiris.cherryweather.data.db.converter

import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.readValue
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.remote.network.provideObjectMapper

object CoordinatesConverter {

    private var mapper = provideObjectMapper()

    @TypeConverter
    @JvmStatic
    fun fromCoordinates(value: String): Weather.Coordinates {
        return mapper.readValue(value)
    }

    @TypeConverter
    @JvmStatic
    fun coordinatesToJson(coordinates: Weather.Coordinates): String {
        return mapper.writeValueAsString(coordinates)
    }
}
