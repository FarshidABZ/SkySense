package com.farshidabz.skysense.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.farshidabz.skysense.database.entity.CityForecastEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailWithForecast
import com.farshidabz.skysense.database.entity.CityWeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM city_weather")
    suspend fun getAll(): List<CityWeatherEntity>

    @Query("SELECT * FROM city_weather WHERE name IN (:names)")
    suspend fun getByNames(names: List<String>): List<CityWeatherEntity>

    @Upsert
    suspend fun upsertAll(items: List<CityWeatherEntity>)

    @Query("DELETE FROM city_weather WHERE name NOT IN (:names)")
    suspend fun deleteNotIn(names: List<String>)

    @Transaction
    @Query("SELECT * FROM city_weather_detail WHERE cityName = :cityName")
    suspend fun getWeatherDetail(cityName: String): CityWeatherDetailWithForecast?

    @Upsert
    suspend fun upsertWeatherDetail(detail: CityWeatherDetailEntity)

    @Upsert
    suspend fun upsertForecasts(forecasts: List<CityForecastEntity>)

    @Query("DELETE FROM city_forecast WHERE cityName = :cityName")
    suspend fun deleteForecasts(cityName: String)
}
