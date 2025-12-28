package com.farshidabz.skysense.network

import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.model.CityWeatherDTO
import com.farshidabz.skysense.data.model.WeatherDetailDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.coroutines.cancellation.CancellationException

class WeatherApiImpl(
    private val httpClient: HttpClient,
    private val config: NetworkConfig,
) : WeatherApi {
    override suspend fun getCityWeather(city: String): Result<CityWeatherDTO> {
        return try {
            httpClient.get("${config.baseUrl}current.json") {
                parameter("key", config.apiKey)
                parameter("q", city)
            }.toNetworkResult()
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.Error(error = AppError.Network(t.toNetworkError()), cause = t)
        }
    }

    override suspend fun getWeatherDetail(city: String): Result<WeatherDetailDTO> {
        return try {
            httpClient.get("${config.baseUrl}forecast.json") {
                parameter("key", config.apiKey)
                parameter("q", city)
                parameter("days", 7)
            }.toNetworkResult()
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.Error(error = AppError.Network(t.toNetworkError()), cause = t)
        }
    }
}