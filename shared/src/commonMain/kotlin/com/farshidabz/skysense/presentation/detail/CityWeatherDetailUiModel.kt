package com.farshidabz.skysense.presentation.detail

import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import com.farshidabz.skysense.domain.model.ForecastDayBO

data class CityWeatherDetailUiModel(
    val cityName: String,
    val temperature: Int,
    val minTemp: Int,
    val maxTemp: Int,
    val humidity: Int,
    val pressureMb: Int,
    val windKph: Int,
    val visibilityKm: Int,
    val conditionText: String,
    val conditionIcon: String,
    val conditionCode: Int,
    val forecasts: List<ForecastDayUiModel>
) {
    companion object Companion {
        val default = CityWeatherDetailUiModel(
            cityName = "",
            temperature = 0,
            minTemp = 0,
            maxTemp = 0,
            humidity = 0,
            pressureMb = 0,
            windKph = 0,
            visibilityKm = 0,
            conditionText = "",
            conditionIcon = "",
            conditionCode = 0,
            forecasts = emptyList()
        )

        fun fromDomainModel(data: CityWeatherDetailBO) = CityWeatherDetailUiModel(
            cityName = data.cityName,
            temperature = data.temperature.toInt(),
            minTemp = data.minTemp.toInt(),
            maxTemp = data.maxTemp.toInt(),
            humidity = data.humidity,
            pressureMb = data.pressureMb.toInt(),
            windKph = data.windKph.toInt(),
            visibilityKm = data.visibilityKm.toInt(),
            conditionText = data.conditionText,
            conditionIcon = data.conditionIcon,
            conditionCode = data.conditionCode,
            forecasts = data.forecasts.map { ForecastDayUiModel.fromDomainModel(it) }

        )
    }

}
data class ForecastDayUiModel(
    val dayOfWeek: String,
    val maxTempC: Int,
    val minTempC: Int,
    val conditionIcon: String,
    val conditionCode: Int
) {
    companion object {
        fun fromDomainModel(it: ForecastDayBO) = ForecastDayUiModel(
            dayOfWeek = it.dayOfWeek,
            maxTempC = it.maxTempC.toInt(),
            minTempC = it.minTempC.toInt(),
            conditionIcon = it.conditionIcon,
            conditionCode = it.conditionCode
        )
    }
}
