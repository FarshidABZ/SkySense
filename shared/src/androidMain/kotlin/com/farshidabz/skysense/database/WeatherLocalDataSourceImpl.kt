package com.farshidabz.skysense.database

import com.farshidabz.skysense.data.mapper.toDetailEntity
import com.farshidabz.skysense.data.mapper.toForecastEntities
import com.farshidabz.skysense.database.dao.WeatherDao
import com.farshidabz.skysense.database.entity.CityWeatherDetailWithForecast
import com.farshidabz.skysense.database.entity.CityWeatherEntity
import com.farshidabz.skysense.database.mapper.toEntity
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO

class WeatherLocalDataSourceImpl(
    private val dao: WeatherDao,
    private val timeProvider: () -> Long = { System.currentTimeMillis() }
) : WeatherLocalDataSource {

    override suspend fun getAll(): List<CityWeatherEntity> = dao.getAll()

    override suspend fun getByNames(names: List<String>): List<CityWeatherEntity> =
        dao.getByNames(names)

    override suspend fun upsertAll(items: List<CityWeatherBO>) {
        val now = timeProvider()
        dao.upsertAll(items.map { it.toEntity(now) })
    }

    override suspend fun deleteNotIn(names: List<String>) {
        dao.deleteNotIn(names)
    }

    override suspend fun getWeatherDetail(cityName: String): CityWeatherDetailWithForecast? {
        return dao.getWeatherDetail(cityName)
    }

    override suspend fun upsertWeatherDetail(detail: CityWeatherDetailBO) {
        dao.deleteForecasts(detail.cityName)
        dao.upsertWeatherDetail(detail.toDetailEntity())
        dao.upsertForecasts(detail.toForecastEntities())
    }
}
