package com.farshidabz.skysense.database

import com.farshidabz.skysense.database.entity.CityWeatherEntity
import com.farshidabz.skysense.database.mapper.toBo
import com.farshidabz.skysense.database.mapper.toEntity
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.ConditionBO
import kotlin.test.Test
import kotlin.test.assertEquals

class CityWeatherEntityMapperTest {

    @Test
    fun toBo_mapsAllFields() {
        val entity = CityWeatherEntity(
            name = "Tehran",
            temperature = 21.5f,
            country = "IR",
            windSpeed = 5.4f,
            humidity = 30,
            pressure = 1012,
            conditionText = "Sunny",
            conditionIcon = "//cdn",
            conditionCode = 1000,
            updatedAt = 1234L
        )

        val bo = entity.toBo()

        assertEquals("Tehran", bo.name)
        assertEquals(21.5f, bo.temperature)
        assertEquals("IR", bo.country)
        assertEquals(5.4f, bo.windSpeed)
        assertEquals(30, bo.humidity)
        assertEquals(1012, bo.pressure)
        assertEquals("Sunny", bo.condition.text)
        assertEquals("//cdn", bo.condition.icon)
        assertEquals(1000, bo.condition.code)
    }

    @Test
    fun toEntity_mapsAllFields_andSetsUpdatedAt() {
        val now = 99L
        val bo = CityWeatherBO(
            name = "Paris",
            temperature = 18f,
            country = "FR",
            windSpeed = 3.2f,
            humidity = 55,
            pressure = 1005,
            condition = ConditionBO("Cloudy", "//icon", 1003)
        )

        val entity = bo.toEntity(now)

        assertEquals("Paris", entity.name)
        assertEquals(18f, entity.temperature)
        assertEquals("FR", entity.country)
        assertEquals(3.2f, entity.windSpeed)
        assertEquals(55, entity.humidity)
        assertEquals(1005, entity.pressure)
        assertEquals("Cloudy", entity.conditionText)
        assertEquals("//icon", entity.conditionIcon)
        assertEquals(1003, entity.conditionCode)
        assertEquals(now, entity.updatedAt)
    }
}
