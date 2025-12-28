package com.farshidabz.skysense.network

import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.model.CityWeatherDTO
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

class WeatherApiImplTest {

    private fun mockClient(handler: suspend (HttpRequestData) -> Any): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when (val result = handler(request)) {
                        is Pair<*, *> -> {
                            val body = result.first as String
                            val status = result.second as HttpStatusCode
                            respond(
                                content = body,
                                status = status,
                                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            )
                        }
                        is String -> {
                            respond(
                                content = result,
                                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            )
                        }
                        is HttpStatusCode -> {
                            respondError(result)
                        }
                        else -> error("Unsupported mock response type: ${'$'}result")
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        explicitNulls = false
                    }
                )
            }
        }
    }

    @Test
    fun getCityWeather_validParams_callsCorrectEndpoint() {
        runTest {
            val baseUrl = "https://example.com/v1/"
            val apiKey = "TEST_KEY"
            var assertedRequest = false
            val client = mockClient { request ->
                assertEquals("/v1/current.json", request.url.encodedPath)
                assertEquals("TEST_KEY", request.url.parameters["key"])
                assertEquals("Tehran", request.url.parameters["q"])
                assertedRequest = true

                """{"location":{"name":"Tehran","country":"IR"},"current":{}}"""
            }
            val config = NetworkConfig(baseUrl = baseUrl, apiKey = apiKey)
            val api: WeatherApi = WeatherApiImpl(client, config)

            api.getCityWeather("Tehran")

            assertTrue(assertedRequest)
        }
    }

    @Test
    fun getCityWeather_success_parsesResponse() {
        runTest {
            val client = mockClient { SAMPLE_SUCCESS_JSON }
            val api: WeatherApi = WeatherApiImpl(client, NetworkConfig(baseUrl = "https://example.com/v1/", apiKey = "TEST_KEY"))

            val result = api.getCityWeather("Tehran")

            val success = assertIs<Result.Success<CityWeatherDTO>>(result)
            val dto = success.data
            assertEquals("Tehran", dto.location?.name)
            assertEquals("IR", dto.location?.country)
            assertEquals(21.5f, dto.current?.temperature)
            assertEquals("Sunny", dto.current?.condition?.text)
            assertEquals(30, dto.current?.humidity)
            assertEquals(1012f, dto.current?.pressure)
            assertEquals(5.4f, dto.current?.windSpeed)
        }
    }

    @Test
    fun getCityWeather_httpError_mapsToResultError() {
        runTest {
            val client = mockClient {
                HttpStatusCode.NotFound
            }
            val api = WeatherApiImpl(client, NetworkConfig(baseUrl = "https://example.com/v1/", apiKey = "K"))

            val result = api.getCityWeather("UnknownCity")

            val error = assertIs<Result.Error>(result)
            val appError = error.error
            val network = assertIs<AppError.Network>(appError)
            assertEquals(NetworkError.NotFound, network.kind)
        }
    }

    @Test
    fun getCityWeather_malformedJson_mapsToResultErrorWithException() {
        runTest {
            val client = mockClient {
                "{"
            }
            val api = WeatherApiImpl(client, NetworkConfig(baseUrl = "https://example.com/v1/", apiKey = "K"))

            val result = api.getCityWeather("Tehran")

            val error = assertIs<Result.Error>(result)
            assertNotNull(error.cause)
        }
    }

    companion object {
        private const val SAMPLE_SUCCESS_JSON = """
            {"location":{"name":"Tehran","country":"IR"},
             "current":{
               "temp_c":21.5,
               "condition":{"text":"Sunny","icon":"//cdn","code":1000},
               "humidity":30,
               "pressure_mb":1012,
               "wind_kph":5.4
             }
            }
        """
    }
}