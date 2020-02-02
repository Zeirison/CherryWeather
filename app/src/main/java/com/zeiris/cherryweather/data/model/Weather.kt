package com.zeiris.cherryweather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zeiris.cherryweather.data.remote.deserializer.DateDeserializer
import com.zeiris.cherryweather.data.remote.deserializer.WeatherDeserializer
import java.util.*
import kotlin.math.abs


@Entity(tableName = "weather")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = WeatherDeserializer::class)
data class Weather(
    @PrimaryKey val id: Int,
    val name: String,
    @JsonProperty(value = "coord")
    val coordinates: Coordinates,
    val country: String,
    val timezone: Int,
    @JsonDeserialize(using = DateDeserializer::class)
    val sunrise: Date,
    @JsonDeserialize(using = DateDeserializer::class)
    val sunset: Date,
    var forecast: List<Forecast>
) {
    data class Coordinates(
        val lat: Double,
        val lon: Double
    )

    fun getNearestForecast(): Forecast {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, -calendar.timeZone.getOffset(calendar.timeInMillis))
        val date = calendar.time

        var minDiff: Long = -1
        var nearestForecast: Forecast = forecast[0]
        for (fc in forecast) {
            val diff: Long = abs(date.time - fc.date.time)
            if (minDiff == -1L || diff < minDiff) {
                minDiff = diff
                nearestForecast = fc
            }
        }
        return nearestForecast
    }
}