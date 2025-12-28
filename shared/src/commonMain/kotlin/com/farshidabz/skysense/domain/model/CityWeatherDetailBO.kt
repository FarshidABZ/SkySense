package com.farshidabz.skysense.domain.model


data class CityWeatherDetailBO(
    val cityName: String,
    val temperature: Float,
    val minTemp: Float,
    val maxTemp: Float,
    val humidity: Int,
    val pressureMb: Float,
    val windKph: Float,
    val visibilityKm: Float,
    val conditionText: String,
    val conditionIcon: String,
    val conditionCode: Int,
    val forecasts: List<ForecastDayBO>
)

data class ForecastDayBO(
    val dayOfWeek: String,
    val maxTempC: Float,
    val minTempC: Float,
    val conditionIcon: String,
    val conditionCode: Int
)
