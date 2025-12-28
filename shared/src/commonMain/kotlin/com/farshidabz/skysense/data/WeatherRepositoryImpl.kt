package com.farshidabz.skysense.data

import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.data.mapper.toDomainModel
import com.farshidabz.skysense.data.model.CityWeatherDTO
import com.farshidabz.skysense.database.CityDataSource
import com.farshidabz.skysense.database.WeatherLocalDataSource
import com.farshidabz.skysense.database.mapper.toBo
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import com.farshidabz.skysense.domain.repository.WeatherRepository
import com.farshidabz.skysense.network.WeatherApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.supervisorScope

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherApi,
    private val cityDataSource: CityDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {
    override fun getDashboardWeather(): Flow<Result<List<CityWeatherBO>>> = flow {
        val cities = cityDataSource.getCities()
        if (cities.isEmpty()) {
            emit(Result.Success(emptyList()))
            return@flow
        }

        // 1. Emit Cache Immediately, to show already cached data to the user
        // while waiting for new data from server
        val cached = localDataSource.getByNames(cities)
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toBo() }))
        }

        // 2. Fetch Remote to update the cache and provided updated data
        val results = fetchFromRemote(cities)
        val freshData = getSuccesses(results)

        if (freshData.isNotEmpty()) {
            updateCache(freshData, cities)
            emit(Result.Success(freshData.sortedBy { it.name }))
        } else {
            // 4. Handle Failure, Warnings, and Error
            val errors = results.mapNotNull { (it as? Result.Error) }

            if (errors.isNotEmpty()) {
                emit(Result.Error(error = errors.first().error, cause = errors.first().cause))
            }
        }
    }.flowOn(dispatcher)

    override fun getWeatherDetail(cityName: String): Flow<Result<CityWeatherDetailBO>> = flow {
        val cached = localDataSource.getWeatherDetail(cityName)
        if (cached != null) {
            emit(Result.Success(cached.toDomainModel()))
        }

        when (val result = remoteDataSource.getWeatherDetail(cityName)) {
            is Result.Success -> {
                val weatherDetail = result.data.toDomainModel()
                localDataSource.upsertWeatherDetail(weatherDetail)
                emit(Result.Success(weatherDetail))
            }

            is Result.Error -> {
                emit(Result.Error(error = result.error, cause = result.cause))
            }
        }
    }.flowOn(dispatcher)

    private suspend fun fetchFromRemote(cities: List<String>): List<Result<CityWeatherDTO>> =
        supervisorScope {
            cities.map { city ->
                async {
                    remoteDataSource.getCityWeather(city)
                }
            }
        }.awaitAll()

    private fun getSuccesses(results: List<Result<CityWeatherDTO>>): List<CityWeatherBO> {
        return results.mapNotNull { res ->
            (res as? Result.Success)?.data?.toDomainModel()
        }
    }

    private suspend fun updateCache(
        data: List<CityWeatherBO>,
        cities: List<String>
    ) {
        if (data.isNotEmpty()) {
            localDataSource.upsertAll(data)
            localDataSource.deleteNotIn(cities)
        }
    }
}