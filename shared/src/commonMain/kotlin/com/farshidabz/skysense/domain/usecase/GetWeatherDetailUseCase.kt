package com.farshidabz.skysense.domain.usecase

import com.farshidabz.skysense.domain.repository.WeatherRepository

class GetWeatherDetailUseCase(private val repository: WeatherRepository) {
    operator fun invoke(cityName: String) = repository.getWeatherDetail(cityName)
}