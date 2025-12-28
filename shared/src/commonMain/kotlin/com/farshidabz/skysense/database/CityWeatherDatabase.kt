package com.farshidabz.skysense.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farshidabz.skysense.database.dao.WeatherDao
import com.farshidabz.skysense.database.entity.CityForecastEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailEntity
import com.farshidabz.skysense.database.entity.CityWeatherEntity

@Database(
    entities = [
        CityWeatherEntity::class,
        CityWeatherDetailEntity::class,
        CityForecastEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CityWeatherDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): WeatherDao
}
