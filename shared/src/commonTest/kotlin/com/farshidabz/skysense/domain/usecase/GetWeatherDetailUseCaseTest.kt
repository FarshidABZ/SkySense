package com.farshidabz.skysense.domain.usecase

import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import com.farshidabz.skysense.domain.repository.WeatherRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class GetWeatherDetailUseCaseTest {

    @Test
    fun invoke_forwardsCityName_andPropagatesResult() = runTest {
        val city = "Paris"
        val expected = Result.Success(
            CityWeatherDetailBO(
                cityName = city,
                temperature = 18f,
                minTemp = 12f,
                maxTemp = 20f,
                humidity = 50,
                pressureMb = 1015f,
                windKph = 8f,
                visibilityKm = 10f,
                conditionText = "Sunny",
                conditionIcon = "//icon",
                conditionCode = 1000,
                forecasts = emptyList()
            )
        )
        val repo = mock<WeatherRepository>()
        every { repo.getWeatherDetail(city) } returns flowOf(expected)

        val useCase = GetWeatherDetailUseCase(repo)

        val emissions = mutableListOf<Result<CityWeatherDetailBO>>()
        useCase(city).toList(emissions)

        val only = assertIs<Result.Success<CityWeatherDetailBO>>(emissions.single())
        assertSame(expected, only)
    }
}
