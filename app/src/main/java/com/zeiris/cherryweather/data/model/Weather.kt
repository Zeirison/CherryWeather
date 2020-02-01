package com.zeiris.cherryweather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zeiris.cherryweather.data.remote.deserializer.DateDeserializer
import com.zeiris.cherryweather.data.remote.deserializer.WeatherDeserializer
import java.util.*

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
}