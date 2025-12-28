package com.farshidabz.skysense.domain.usecase

import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.ConditionBO
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

class GetDashboardWeatherUseCaseTest {

    @Test
    fun invoke_delegatesToRepository_andPropagatesValues() = runTest {
        val repo = mock<WeatherRepository>()
        val expected = Result.Success(
            listOf(
                CityWeatherBO(
                    name = "Amsterdam",
                    temperature = 10f,
                    country = "NL",
                    windSpeed = 5f,
                    humidity = 60,
                    pressure = 1010,
                    condition = ConditionBO("Clear", "//icon", 1000)
                )
            )
        )
        every { repo.getDashboardWeather() } returns flowOf(expected)

        val useCase = GetDashboardWeatherUseCase(repo)

        val emissions = mutableListOf<Result<List<CityWeatherBO>>>()
        useCase().toList(emissions)

        val only = assertIs<Result.Success<List<CityWeatherBO>>>(emissions.single())
        assertSame(expected, only)
    }
}
