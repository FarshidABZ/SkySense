package com.farshidabz.skysense.database

import com.farshidabz.skysense.database.entity.CityWeatherDetailWithForecast
import com.farshidabz.skysense.database.entity.CityWeatherEntity
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO

interface WeatherLocalDataSource {
    suspend fun getAll(): List<CityWeatherEntity>
    suspend fun getByNames(names: List<String>): List<CityWeatherEntity>
    suspend fun upsertAll(items: List<CityWeatherBO>)
    suspend fun deleteNotIn(names: List<String>)
    suspend fun getWeatherDetail(cityName: String): CityWeatherDetailWithForecast?
    suspend fun upsertWeatherDetail(detail: CityWeatherDetailBO)
}