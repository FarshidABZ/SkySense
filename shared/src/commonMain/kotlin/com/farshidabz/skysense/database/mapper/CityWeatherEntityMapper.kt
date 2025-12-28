package com.farshidabz.skysense.database.mapper

import com.farshidabz.skysense.database.entity.CityWeatherEntity
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.ConditionBO

fun CityWeatherEntity.toBo(): CityWeatherBO = CityWeatherBO(
    name = name,
    temperature = temperature,
    country = country,
    windSpeed = windSpeed,
    humidity = humidity,
    pressure = pressure,
    condition = ConditionBO(
        text = conditionText,
        icon = conditionIcon,
        code = conditionCode
    )
)

fun CityWeatherBO.toEntity(now: Long): CityWeatherEntity = CityWeatherEntity(
    name = name,
    temperature = temperature,
    country = country,
    windSpeed = windSpeed,
    humidity = humidity,
    pressure = pressure,
    conditionText = condition.text,
    conditionIcon = condition.icon,
    conditionCode = condition.code,
    updatedAt = now
)
