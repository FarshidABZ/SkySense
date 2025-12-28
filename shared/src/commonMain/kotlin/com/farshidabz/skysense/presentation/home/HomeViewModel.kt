package com.farshidabz.skysense.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import com.farshidabz.skysense.domain.model.CityWeatherBO
import com.farshidabz.skysense.domain.usecase.GetDashboardWeatherUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    getDashboardWeatherUseCase: GetDashboardWeatherUseCase
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = getDashboardWeatherUseCase()
        .scan(HomeUiState(isLoading = true), ::reduceState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )

    private fun reduceState(
        previousState: HomeUiState,
        result: Result<List<CityWeatherBO>>
    ): HomeUiState {
        return when (result) {
            is Result.Success -> {
                previousState.copy(
                    isLoading = false,
                    error = null,
                    cityWeatherList = result.data.map { CityWeatherUiModel.fromDomainModel(it) }
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

data class HomeUiState(
    val cityWeatherList: List<CityWeatherUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null
)