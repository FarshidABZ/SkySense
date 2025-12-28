package com.farshidabz.skysense.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityWeatherDTO(
    @SerialName("location")
    val location: LocationDTO? = null,
    @SerialName("current")
    val current: CurrentDTO? = null
)

@Serializable
data class CurrentDTO(
    @SerialName("temp_c")
    val temperature: Float? = null,
    @SerialName("condition")
    val condition: ConditionDTO? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("pressure_mb")
    val pressure: Float? = null,
    @SerialName("wind_kph")
    val windSpeed: Float? = null,
)