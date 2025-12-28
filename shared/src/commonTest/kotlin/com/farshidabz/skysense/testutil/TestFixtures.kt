package com.farshidabz.skysense.testutil

import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.model.CityWeatherDTO
import com.farshidabz.skysense.data.model.ConditionDTO
import com.farshidabz.skysense.data.model.CurrentDTO
import com.farshidabz.skysense.data.model.LocationDTO

fun cityWeatherDto(
    name: String = "City",
    country: String = "CC",
    tempC: Float = 20.0f,
    humidity: Int = 50,
    pressureMb: Float = 1010f,
    windKph: Float = 5.0f,
    conditionText: String = "Clear"
): CityWeatherDTO = CityWeatherDTO(
    location = LocationDTO(name = name, country = country),
    current = CurrentDTO(
        temperature = tempC,
        condition = ConditionDTO(text = conditionText, icon = "//icon", code = 1000),
        humidity = humidity,
        pressure = pressureMb,
        windSpeed = windKph
    )
)

fun <T> success(data: T): Result.Success<T> = Result.Success(data)

fun error(appError: AppError = AppError.Unknown, cause: Throwable? = null): Result.Error =
    Result.Error(error = appError, cause = cause)
