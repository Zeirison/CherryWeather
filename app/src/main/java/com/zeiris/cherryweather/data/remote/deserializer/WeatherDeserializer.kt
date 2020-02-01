package com.zeiris.cherryweather.data.remote.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.NumericNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.data.remote.network.provideObjectMapper
import java.util.*

class WeatherDeserializer : StdDeserializer<Weather>(Weather::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Weather? {
        val node = p?.codec?.readTree<TreeNode>(p)
        return node?.let { setupWeather(it) }
    }

    private fun setupWeather(node: TreeNode): Weather {
        val cityNode = node.get("city")
        val coordNode = node.get("city").get("coord")
        val forecastNode = node.get("list")

        val mapper = provideObjectMapper()

        val coord = Weather.Coordinates(
            lat = (coordNode.get("lat") as NumericNode).asDouble(),
            lon = (coordNode.get("lon") as NumericNode).asDouble()
        )

        return Weather(
            id = (cityNode.get("id") as NumericNode).asInt(),
            name = (cityNode.get("name") as TextNode).asText(),
            coordinates = coord,
            country = (cityNode.get("country") as TextNode).asText(),
            timezone = (cityNode.get("timezone") as NumericNode).asInt(),
            sunrise = nodeToDate(cityNode.get("sunrise")),
            sunset = nodeToDate(cityNode.get("sunset")),
            forecast = mapper.readValue(forecastNode.toString())
        )
    }

    private fun nodeToDate(node: TreeNode): Date {
        return Date((node as NumericNode).asLong() * 1000L)
    }
}