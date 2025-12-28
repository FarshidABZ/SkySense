package com.farshidabz.skysense.domain.usecase

import com.farshidabz.skysense.domain.repository.WeatherRepository

class GetDashboardWeatherUseCase(private val repository: WeatherRepository) {
    operator fun invoke() = repository.getDashboardWeather()
}