package com.farshidabz.skysense.di

import com.farshidabz.skysense.database.databaseAndroidModule
import com.farshidabz.skysense.presentation.detail.WeatherDetailViewModel
import com.farshidabz.skysense.presentation.home.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule: Module = module {
    viewModelOf(::HomeViewModel)
    viewModel { (cityName: String) ->
        WeatherDetailViewModel(
            savedStateHandle = get(),
            cityName = cityName,
            getWeatherDetailUseCase = get()
        )
    }
}

fun sharedModulesForAndroid(): List<Module> =
    commonModules() + databaseAndroidModule + presentationModule
