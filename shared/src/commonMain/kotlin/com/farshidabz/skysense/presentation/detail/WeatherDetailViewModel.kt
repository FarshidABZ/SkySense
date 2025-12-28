package com.farshidabz.skysense.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.domain.model.CityWeatherDetailBO
import com.farshidabz.skysense.domain.usecase.GetWeatherDetailUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class WeatherDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    cityName: String,
    getWeatherDetailUseCase: GetWeatherDetailUseCase
) : ViewModel() {
    private val cityName: String =
        savedStateHandle.get<String>("cityName") ?: cityName.also {
            savedStateHandle["cityName"] = it
        }

    val uiState: StateFlow<WeatherDetailUiState> = getWeatherDetailUseCase(this.cityName)
        .scan(WeatherDetailUiState(isLoading = true), ::reduceState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WeatherDetailUiState(isLoading = true)
        )

    private fun reduceState(
        previousState: WeatherDetailUiState,
        result: Result<CityWeatherDetailBO>
    ): WeatherDetailUiState {
        return when (result) {
            is Result.Success -> {
                previousState.copy(
                    isLoading = false,
                    error = null,
                    weather = CityWeatherDetailUiModel.fromDomainModel(result.data)
                )
            }

            is Result.Error -> {
                previousState.copy(
                    isLoading = false,
                    error = result.error
                )
            }
        }
    }
}

data class WeatherDetailUiState(
    val isLoading: Boolean = false,
    val weather: CityWeatherDetailUiModel = CityWeatherDetailUiModel.default,
    val error: AppError? = null
)