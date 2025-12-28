package com.farshidabz.skysense.data

import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.model.ConditionDTO
import com.farshidabz.skysense.data.model.CurrentWeatherDTO
import com.farshidabz.skysense.data.model.DayDTO
import com.farshidabz.skysense.data.model.ForecastDTO
import com.farshidabz.skysense.data.model.ForecastDayDTO
import com.farshidabz.skysense.data.model.LocationDTO
import com.farshidabz.skysense.data.model.WeatherDetailDTO
import com.farshidabz.skysense.database.CityDataSource
import com.farshidabz.skysense.database.WeatherLocalDataSource
import com.farshidabz.skysense.database.entity.CityForecastEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailEntity
import com.farshidabz.skysense.database.entity.CityWeatherDetailWithForecast
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import com.farshidabz.skysense.network.WeatherApi
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.matcher.any
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WeatherRepositoryImplDetailTest {

    private fun repoWithDetail(
        city: String,
        cached: CityWeatherDetailWithForecast?,
        remote: Result<WeatherDetailDTO>
    ): WeatherRepositoryImpl {
        val api = mock<WeatherApi>()
        everySuspend { api.getWeatherDetail(city) } returns remote

        val local = mock<WeatherLocalDataSource>()
        everySuspend { local.getWeatherDetail(city) } returns cached
        everySuspend { local.upsertWeatherDetail(any()) } returns Unit
        everySuspend { local.getByNames(any()) } returns emptyList()
        everySuspend { local.getAll() } returns emptyList()
        everySuspend { local.upsertAll(any()) } returns Unit
        everySuspend { local.deleteNotIn(any()) } returns Unit

        val cityDs = mock<CityDataSource>()
        every { cityDs.getCities() } returns emptyList()

        return WeatherRepositoryImpl(api, cityDs, local, Dispatchers.Unconfined)
    }

    @Test
    fun getWeatherDetail_cacheThenFresh_success() = runTest {
        val city = "Paris"
        val cached = CityWeatherDetailWithForecast(
            detail = CityWeatherDetailEntity(
                cityName = city,
                temperature = 18f,
                minTemp = 12f,
                maxTemp = 20f,
                humidity = 40,
                pressureMb = 1011f,
                windKph = 7f,
                visibilityKm = 10f,
                conditionText = "Cloudy",
                conditionIcon = "//icon",
                conditionCode = 1003
            ),
            forecasts = listOf(
                CityForecastEntity(cityName = city, dayOfWeek = "Mon", maxTempC = 20f, minTempC = 12f, conditionIcon = "//i", conditionCode = 1003)
            )
        )
        val remote = Result.Success(sampleDetailDto(city))
        val repo = repoWithDetail(city, cached, remote)

        val emissions = mutableListOf<Result<CityWeatherDetailBO>>()
        repo.getWeatherDetail(city).toList(emissions)

        assertEquals(2, emissions.size)
        assertIs<Result.Success<CityWeatherDetailBO>>(emissions[0])
        val second = assertIs<Result.Success<CityWeatherDetailBO>>(emissions[1])
        assertEquals(city, second.data.cityName)
        assertEquals("Sunny", second.data.conditionText)
    }

    @Test
    fun getWeatherDetail_remoteError_emitsError() = runTest {
        val city = "Oslo"
        val repo = repoWithDetail(city, cached = null, remote = Result.Error(AppError.Unknown, IllegalStateException("boom")))

        val emissions = mutableListOf<Result<CityWeatherDetailBO>>()
        repo.getWeatherDetail(city).toList(emissions)

        val only = assertIs<Result.Error>(emissions.single())
        assertEquals(AppError.Unknown, only.error)
        assertEquals("boom", only.cause?.message)
    }

    private fun sampleDetailDto(city: String): WeatherDetailDTO = WeatherDetailDTO(
        location = LocationDTO(name = city, country = "FR"),
        current = CurrentWeatherDTO(
            lastUpdatedEpoch = 0,
            lastUpdated = "",
            tempC = 21f,
            tempF = 0f,
            isDay = 1,
            condition = ConditionDTO(text = "Sunny", icon = "//icon", code = 1000),
            windMph = 0f,
            windKph = 10f,
            windDegree = 0,
            windDir = "N",
            pressureMb = 1015f,
            pressureIn = 0f,
            precipMm = 0f,
            precipIn = 0f,
            humidity = 30,
            cloud = 0,
            feelslikeC = 0f,
            feelslikeF = 0f,
            visKm = 10f,
            visMiles = 0f,
            uv = 0f,
            gustMph = 0f,
            gustKph = 0f
        ),
        forecast = ForecastDTO(
            forecastday = listOf(
                ForecastDayDTO(
                    date = "2024-01-01",
                    dateEpoch = 1704067200,
                    day = DayDTO(
                        maxtempC = 22f,
                        maxtempF = 0f,
                        mintempC = 15f,
                        mintempF = 0f,
                        avgtempC = 0f,
                        avgtempF = 0f,
                        maxwindMph = 0f,
                        maxwindKph = 0f,
                        totalprecipMm = 0f,
                        totalprecipIn = 0f,
                        avgvisKm = 0f,
                        avgvisMiles = 0f,
                        avghumidity = 0,
                        dailyWillItRain = 0,
                        dailyChanceOfRain = 0,
                        dailyWillItSnow = 0,
                        dailyChanceOfSnow = 0,
                        condition = ConditionDTO(text = "Sunny", icon = "//icon", code = 1000),
                        uv = 0f
                    )
                )
            )
        )
    )
}
