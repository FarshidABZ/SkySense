package com.farshidabz.skysense.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "city_forecast",
    foreignKeys = [ForeignKey(
        entity = CityWeatherDetailEntity::class,
        parentColumns = ["cityName"],
        childColumns = ["cityName"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cityName")]
)
data class CityForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityName: String,
    val dayOfWeek: String,
    val maxTempC: Float,
    val minTempC: Float,
    val conditionIcon: String,
    val conditionCode: Int,
)

data class CityWeatherDetailWithForecast(
    @Embedded val detail: CityWeatherDetailEntity,
    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val forecasts: List<CityForecastEntity>
)