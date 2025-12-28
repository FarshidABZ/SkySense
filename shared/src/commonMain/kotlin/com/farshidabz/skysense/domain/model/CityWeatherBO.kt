package com.farshidabz.skysense.domain.model

data class CityWeatherBO(
    val name: String,
    val temperature: Float,
    val country: String,
    val windSpeed: Float,
    val humidity: Int,
    val pressure: Int,
    val condition: ConditionBO,
)

data class ConditionBO(
    val text: String,
    val icon: String,
    val code: Int,
) {
    companion object {
        fun default() = ConditionBO("", "", 0)
    }
}