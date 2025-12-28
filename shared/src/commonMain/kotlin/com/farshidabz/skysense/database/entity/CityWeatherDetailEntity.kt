package com.farshidabz.skysense.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_weather_detail")
data class CityWeatherDetailEntity(
    @PrimaryKey val cityName: String,
    val temperature: Float,
    val minTemp: Float,
    val maxTemp: Float,
    val humidity: Int,
    val pressureMb: Float,
    val windKph: Float,
    val visibilityKm: Float,
    val conditionText: String,
    val conditionIcon: String,
    val conditionCode: Int,
)