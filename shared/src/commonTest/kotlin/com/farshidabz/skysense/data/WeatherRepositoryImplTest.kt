package com.farshidabz.skysense.data

import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.model.CityWeatherDTO
import com.farshidabz.skysense.database.CityDataSource
import com.farshidabz.skysense.database.WeatherLocalDataSource
import com.farshidabz.skysense.database.entity.CityWeatherEntity
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.network.WeatherApi
import com.farshidabz.skysense.testutil.cityWeatherDto
import com.farshidabz.skysense.testutil.success
import com.farshidabz.skysense.testutil.error
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.matcher.any
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class WeatherRepositoryImplTest {
    private fun makeRepo(
        cities: List<String>,
        apiResponses: Map<String, Result<CityWeatherDTO>>,
        cached: List<CityWeatherEntity> = emptyList(),
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
    ): WeatherRepositoryImpl {
        val cityDataSource = mock<CityDataSource>()
        every { cityDataSource.getCities() } returns cities

        val local = mock<WeatherLocalDataSource>()
        everySuspend { local.getByNames(any()) } returns cached
        everySuspend { local.upsertAll(any()) } returns Unit
        everySuspend { local.deleteNotIn(any()) } returns Unit
        everySuspend { local.getAll() } returns cached

        val api = mock<WeatherApi>()
        cities.forEach { city ->
            val res = apiResponses[city] ?: Result.Error(error = AppError.Unknown)
            everySuspend { api.getCityWeather(city) } returns res
        }

        return WeatherRepositoryImpl(api, cityDataSource, local, dispatcher)
    }

    @Test
    fun getDashboardWeather_noCities_returnsSuccessEmptyList() {
        runTest {
            val repo = makeRepo(emptyList(), emptyMap())

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            val only = assertIs<Result.Success<List<CityWeatherBO>>>(emissions.single())
            assertTrue(only.data.isEmpty())
        }
    }

    @Test
    fun getDashboardWeather_cacheEmittedThenRemoteSuccess_updatesAndEmitsFresh() {
        runTest {
            val cities = listOf("Amsterdam", "Tokyo")
            val cached = listOf(
                getCityWeatherEntity(name = "Amsterdam", country = "NL", temp = 5f),
                getCityWeatherEntity(name = "Tokyo", country = "JP", temp = 15f)
            )
            val responses = mapOf(
                "Amsterdam" to success(cityWeatherDto(name = "Amsterdam", country = "NL", tempC = 12.0f)),
                "Tokyo" to success(cityWeatherDto(name = "Tokyo", country = "JP", tempC = 20.0f))
            )

            val repo = makeRepo(cities, responses, cached = cached)

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            assertEquals(2, emissions.size)
            val first = assertIs<Result.Success<List<CityWeatherBO>>>(emissions[0])
            assertEquals(listOf("Amsterdam", "Tokyo"), first.data.map { it.name })
            assertEquals(listOf(5f, 15f), first.data.map { it.temperature })

            val second = assertIs<Result.Success<List<CityWeatherBO>>>(emissions[1])
            assertEquals(listOf("Amsterdam", "Tokyo"), second.data.map { it.name })
            assertEquals(listOf(12.0f, 20.0f), second.data.map { it.temperature })
        }
    }

    @Test
    fun getDashboardWeather_allSuccess_freshKeepsInputOrder() {
        runTest {
            val cities = listOf("Amsterdam", "Dubai", "Tokyo")
            val responses = mapOf(
                "Amsterdam" to success(cityWeatherDto(name = "Amsterdam", country = "NL")),
                "Tokyo" to success(cityWeatherDto(name = "Tokyo", country = "JP")),
                "Dubai" to success(cityWeatherDto(name = "Dubai", country = "AE"))
            )

            val repo = makeRepo(cities, responses)

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            val last = assertIs<Result.Success<List<CityWeatherBO>>>(emissions.last())
            val list = last.data
            assertEquals(3, list.size)
            assertEquals("Amsterdam", list[0].name)
            assertEquals("Dubai", list[1].name)
            assertEquals("Tokyo", list[2].name)
        }
    }

    @Test
    fun getDashboardWeather_success_mapsFieldsForSingleCity() {
        runTest {
            val cities = listOf("Amsterdam")
            val responses = mapOf(
                "Amsterdam" to success(
                    cityWeatherDto(
                        name = "Amsterdam",
                        country = "NL",
                        tempC = 12.5f,
                        humidity = 70,
                        pressureMb = 1016f,
                        windKph = 10.2f,
                        conditionText = "Cloudy"
                    )
                )
            )

            val repo = makeRepo(cities, responses)

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            val success = assertIs<Result.Success<List<CityWeatherBO>>>(emissions.last())
            val item = success.data.single()
            assertEquals("Amsterdam", item.name)
            assertEquals("NL", item.country)
            assertEquals(12.5f, item.temperature)
            assertEquals(10.2f, item.windSpeed)
            assertEquals(70, item.humidity)
            assertEquals(1016, item.pressure)
        }
    }

    @Test
    fun getDashboardWeather_remoteAllErrors_noCache_emitsError() {
        runTest {
            val cities = listOf("Amsterdam", "Tokyo")
            val expected = error(appError = AppError.Unknown, cause = IllegalStateException("boom"))
            val responses = mapOf(
                "Amsterdam" to expected,
                "Tokyo" to expected,
            )

            val repo = makeRepo(cities, responses)

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            val err = assertIs<Result.Error>(emissions.single())
            assertEquals(expected.error, err.error)
            val cause = err.cause
            assertIs<IllegalStateException>(cause)
            assertEquals("boom", cause.message)
        }
    }

    @Test
    fun getDashboardWeather_remoteErrors_withCache_emitsCacheThenError() {
        runTest {
            val cities = listOf("Amsterdam", "Tokyo")
            val cached = listOf(
                getCityWeatherEntity(name = "Amsterdam", country = "NL", temp = 5f),
                getCityWeatherEntity(name = "Tokyo", country = "JP", temp = 15f)
            )
            val expected = error(appError = AppError.Unknown, cause = RuntimeException("oops"))
            val responses = mapOf(
                "Amsterdam" to expected,
                "Tokyo" to expected,
            )

            val repo = makeRepo(cities, responses, cached = cached)

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            assertEquals(2, emissions.size)
            val first = assertIs<Result.Success<List<CityWeatherBO>>>(emissions[0])
            assertEquals(listOf("Amsterdam", "Tokyo"), first.data.map { it.name })

            val second = assertIs<Result.Error>(emissions[1])
            assertEquals(expected.error, second.error)
            val cause2 = second.cause
            assertIs<RuntimeException>(cause2)
            assertEquals("oops", cause2.message)
        }
    }

    @Test
    fun getDashboardWeather_success_withNullSections_mapsToDefaults() {
        runTest {
            val cities = listOf("EmptyCity")
            val dtoWithNulls = CityWeatherDTO(location = null, current = null)
            val responses = mapOf(
                "EmptyCity" to success(dtoWithNulls)
            )

            val repo = makeRepo(cities, responses)

            val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
            repo.getDashboardWeather().toList(emissions)

            val success = assertIs<Result.Success<List<CityWeatherBO>>>(emissions.last())
            val item = success.data.single()
            assertEquals("", item.name)
            assertEquals("", item.country)
            assertEquals(0f, item.temperature)
            assertEquals(0f, item.windSpeed)
            assertEquals(0, item.humidity)
            assertEquals(0, item.pressure)
            assertEquals("", item.condition.text)
            assertEquals("", item.condition.icon)
            assertEquals(0, item.condition.code)
        }
    }

    private fun getCityWeatherEntity(
        name: String,
        country: String = "CC",
        temp: Float = 1f,
        wind: Float = 1f,
        humidity: Int = 1,
        pressure: Int = 1,
        condText: String = "",
        condIcon: String = "",
        condCode: Int = 0,
        updatedAt: Long = 0L,
    ) = CityWeatherEntity(
        name = name,
        temperature = temp,
        country = country,
        windSpeed = wind,
        humidity = humidity,
        pressure = pressure,
        conditionText = condText,
        conditionIcon = condIcon,
        conditionCode = condCode,
        updatedAt = updatedAt
    )
}
