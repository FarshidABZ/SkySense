package com.farshidabz.skysense.presentation.home

import com.farshidabz.skysense.domain.model.CityWeatherBO

data class CityWeatherUiModel(
    val name: String,
    val temperature: Int,
    val country: String,
) {
    companion object {
        fun fromDomainModel(bo: CityWeatherBO) = CityWeatherUiModel(
            name = bo.name,
            temperature = bo.temperature.toInt(),
            country = bo.country
        )
    }
}