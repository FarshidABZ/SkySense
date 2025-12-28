package com.farshidabz.skysense.domain.repository

import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getDashboardWeather(): Flow<Result<List<CityWeatherBO>>>
    fun getWeatherDetail(cityName: String): Flow<Result<CityWeatherDetailBO>>
}