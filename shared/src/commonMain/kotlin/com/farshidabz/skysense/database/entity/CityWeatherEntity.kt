package com.farshidabz.skysense.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_weather")
data class CityWeatherEntity(
    @PrimaryKey val name: String,
    val temperature: Float,
    val country: String,
    val windSpeed: Float,
    val humidity: Int,
    val pressure: Int,
    val conditionText: String,
    val conditionIcon: String,
    val conditionCode: Int,
    val updatedAt: Long,
)
