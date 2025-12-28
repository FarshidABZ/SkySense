package com.farshidabz.skysense.data.mapper

import com.farshidabz.skysense.data.model.CityWeatherDTO
import com.farshidabz.skysense.data.model.WeatherDetailDTO
import com.farshidabz.skysense.database.entity.CityForecastEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailWithForecast
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import com.farshidabz.skysense.domain.model.ConditionBO
import com.farshidabz.skysense.domain.model.ForecastDayBO
import com.farshidabz.skysense.util.orZero
import java.util.Calendar

fun CityWeatherDTO?.toDomainModel(): CityWeatherBO? {
    return this?.let {
        CityWeatherBO(
            name = it.location?.name.orEmpty(),
            temperature = it.current?.temperature.orZero(),
            country = it.location?.country.orEmpty(),
            windSpeed = it.current?.windSpeed.orZero(),
            humidity = it.current?.humidity.orZero(),
            pressure = it.current?.pressure.orZero().toInt(),
            condition = if (it.current?.condition == null)
                ConditionBO.default()
            else {
                ConditionBO(
                    text = it.current.condition.text.orEmpty(),
                    icon = it.current.condition.icon.orEmpty(),
                    code = it.current.condition.code.orZero()
                )
            }
        )
    }
}

fun WeatherDetailDTO.toDomainModel(): CityWeatherDetailBO {
    return CityWeatherDetailBO(
        cityName = location.name.toString(),
        temperature = current.tempC,
        minTemp = forecast.forecastday.firstOrNull()?.day?.mintempC ?: current.tempC,
        maxTemp = forecast.forecastday.firstOrNull()?.day?.maxtempC ?: current.tempC,
        humidity = current.humidity,
        pressureMb = current.pressureMb,
        windKph = current.windKph,
        visibilityKm = current.visKm,
        conditionText = current.condition.text.toString(),
        conditionIcon = current.condition.icon.toString(),
        conditionCode = current.condition.code.orZero(),
        forecasts = forecast.forecastday.map { forecastDay ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = forecastDay.dateEpoch * 1000
            }
            val dayName = calendar.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                java.util.Locale.getDefault()
            ) ?: ""

            ForecastDayBO(
                dayOfWeek = dayName,
                maxTempC = forecastDay.day.maxtempC,
                minTempC = forecastDay.day.mintempC,
                conditionIcon = forecastDay.day.condition.icon.toString(),
                conditionCode = forecastDay.day.condition.code.orZero()
            )
        }
    )
}

fun CityWeatherDetailBO.toDetailEntity(): CityWeatherDetailEntity {
    return CityWeatherDetailEntity(
        cityName = cityName,
        temperature = temperature,
        minTemp = minTemp,
        maxTemp = maxTemp,
        humidity = humidity,
        pressureMb = pressureMb,
        windKph = windKph,
        visibilityKm = visibilityKm,
        conditionText = conditionText,
        conditionIcon = conditionIcon,
        conditionCode = conditionCode
    )
}

fun CityWeatherDetailBO.toForecastEntities(): List<CityForecastEntity> {
    return forecasts.map { forecast ->
        CityForecastEntity(
            cityName = cityName,
            dayOfWeek = forecast.dayOfWeek,
            maxTempC = forecast.maxTempC,
            minTempC = forecast.minTempC,
            conditionIcon = forecast.conditionIcon,
            conditionCode = forecast.conditionCode
        )
    }
}

fun CityWeatherDetailWithForecast.toDomainModel(): CityWeatherDetailBO {
    return CityWeatherDetailBO(
        cityName = detail.cityName,
        temperature = detail.temperature,
        minTemp = detail.minTemp,
        maxTemp = detail.maxTemp,
        humidity = detail.humidity,
        pressureMb = detail.pressureMb,
        windKph = detail.windKph,
        visibilityKm = detail.visibilityKm,
        conditionText = detail.conditionText,
        conditionIcon = detail.conditionIcon,
        conditionCode = detail.conditionCode,
        forecasts = forecasts.map { forecast ->
            ForecastDayBO(
                dayOfWeek = forecast.dayOfWeek,
                maxTempC = forecast.maxTempC,
                minTempC = forecast.minTempC,
                conditionIcon = forecast.conditionIcon,
                conditionCode = forecast.conditionCode
            )
        }
    )
}
