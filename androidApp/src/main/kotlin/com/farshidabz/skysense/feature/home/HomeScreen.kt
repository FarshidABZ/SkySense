package com.farshidabz.skysense.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farshidabz.skysense.R
import com.farshidabz.skysense.designsystem.components.LoadingIndicator
import com.farshidabz.skysense.designsystem.snackbar.SnackbarManager
import com.farshidabz.skysense.designsystem.theme.CardPaletteTheme
import com.farshidabz.skysense.designsystem.theme.onCardColor
import com.farshidabz.skysense.presentation.home.CityWeatherUiModel
import com.farshidabz.skysense.presentation.home.HomeUiState
import com.farshidabz.skysense.presentation.home.HomeViewModel
import com.farshidabz.skysense.util.asStringRes
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(onCityClick: (String) -> Unit, viewModel: HomeViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState, onCityClick)
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onCityClick: (String) -> Unit,
) {
    if (uiState.isLoading) {
        LoadingIndicator()
    } else {
        val lastShownErrorResId = rememberSaveable { mutableStateOf<Int?>(null) }
        LaunchedEffect(uiState.error) {
            val error = uiState.error
            if (error != null) {
                val resId = error.asStringRes()
                if (lastShownErrorResId.value != resId) {
                    SnackbarManager.show(messageResId = resId)
                    lastShownErrorResId.value = resId
                }
            } else {
                lastShownErrorResId.value = null
            }
        }

        if (uiState.cityWeatherList.isNotEmpty()) {
            HomeScreenContent(uiState.cityWeatherList, onCityClick)
        }
    }
}

@Composable
fun HomeScreenContent(weatherUiModel: List<CityWeatherUiModel>, onCityClick: (String) -> Unit) {
    val state = rememberLazyGridState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Text(
            text = stringResource(R.string.locations),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = state,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(weatherUiModel) {
                val cardColor = CardPaletteTheme.getColorForCity(it.name)
                CityWeatherCard(it, cardColor, onCityClick)
            }
        }
    }
}

@Composable
fun CityWeatherCard(data: CityWeatherUiModel, cardColor: Color, onCityClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = { onCityClick(data.name) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.temperature_format, data.temperature),
                style = MaterialTheme.typography.displayMedium,
                color = onCardColor,
                fontWeight = FontWeight.Bold
            )

            Column {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = onCardColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = data.country,
                    style = MaterialTheme.typography.bodyLarge,
                    color = onCardColor
                )
            }
        }
    }
}
