package com.zeiris.cherryweather.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zeiris.cherryweather.data.remote.deserializer.DateDeserializer
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Forecast(
    @JsonProperty(value = "dt")
    @JsonDeserialize(using = DateDeserializer::class)
    val date: Date,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Main(
        val temp: Float,
        @JsonProperty(value = "temp_min")
        val tempMin: Float,
        @JsonProperty(value = "temp_max")
        val tempMax: Float,
        @JsonProperty(value = "feels_like")
        val feelsLike: Float,
        val pressure: Int,
        val humidity: Byte
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    data class Clouds(
        val all: Byte
    )

    data class Wind(
        val speed: Float,
        @JsonProperty(value = "deg")
        val degree: Int
    )

}