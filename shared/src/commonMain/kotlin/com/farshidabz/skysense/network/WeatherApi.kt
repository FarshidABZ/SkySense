package com.farshidabz.skysense.network

import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.model.CityWeatherDTO
import com.farshidabz.skysense.data.model.WeatherDetailDTO

interface WeatherApi {
    suspend fun getCityWeather(city: String): Result<CityWeatherDTO>
    suspend fun getWeatherDetail(city: String): Result<WeatherDetailDTO>
}
