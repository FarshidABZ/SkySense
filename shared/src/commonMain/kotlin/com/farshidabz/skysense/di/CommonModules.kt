package com.farshidabz.skysense.di

import com.farshidabz.skysense.data.WeatherRepositoryImpl
import com.farshidabz.skysense.database.CityDataSource
import com.farshidabz.skysense.database.CityDataSourceImpl
import com.farshidabz.skysense.database.WeatherLocalDataSource
import com.farshidabz.skysense.domain.repository.WeatherRepository
import com.farshidabz.skysense.domain.usecase.GetDashboardWeatherUseCase
import com.farshidabz.skysense.domain.usecase.GetWeatherDetailUseCase
import com.farshidabz.skysense.network.NetworkConfig
import com.farshidabz.skysense.network.WeatherApi
import com.farshidabz.skysense.network.WeatherApiImpl
import com.farshidabz.skysense.network.provideHttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val networkModule: Module = module {
    single { NetworkConfig() }
    single { provideHttpClient(get()) }
    factory<WeatherApi> { WeatherApiImpl(get(), get()) }
}

val dataModule: Module = module {
    factoryOf(::WeatherRepositoryImpl) { bind<WeatherRepository>() }
}

val domainModule: Module = module {
    factoryOf(::GetDashboardWeatherUseCase) { bind<GetDashboardWeatherUseCase>() }
    factoryOf(::GetWeatherDetailUseCase) { bind<GetWeatherDetailUseCase>() }
}

val databaseModule: Module = module {
    factoryOf(::CityDataSourceImpl) { bind<CityDataSource>() }
}

val coroutineModule: Module = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
}

fun commonModules(): List<Module> = listOf(
    networkModule,
    dataModule,
    domainModule,
    databaseModule,
    coroutineModule
)
