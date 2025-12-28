package com.farshidabz.skysense.feature.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farshidabz.skysense.R
import com.farshidabz.skysense.designsystem.components.TopBar
import com.farshidabz.skysense.designsystem.components.VerticalProgressBar
import com.farshidabz.skysense.feature.detail.WeatherConditionMapper.getBackgroundResource
import com.farshidabz.skysense.feature.detail.WeatherConditionMapper.getIconResource
import com.farshidabz.skysense.presentation.detail.CityWeatherDetailUiModel
import com.farshidabz.skysense.presentation.detail.ForecastDayUiModel
import com.farshidabz.skysense.presentation.detail.WeatherDetailUiState
import com.farshidabz.skysense.presentation.detail.WeatherDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WeatherDetailRoute(
    cityName: String,
    onBack: () -> Unit,
    viewModel: WeatherDetailViewModel = koinViewModel(key = cityName) {
        parametersOf(cityName)
    }
) {
    BackHandler { onBack() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WeatherDetailScreen(
        uiState = uiState,
        onBack = onBack
    )
}

@Composable
fun WeatherDetailScreen(
    uiState: WeatherDetailUiState,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        WeatherBackgroundImage(conditionCode = uiState.weather.conditionCode)
        WeatherBackgroundGradient()

        WeatherContent(weather = uiState.weather)

        WeatherTopBar(
            cityName = uiState.weather.cityName,
            onBack = onBack
        )
    }
}


@Composable
private fun WeatherBackgroundImage(conditionCode: Int) {
    Image(
        painter = painterResource(getBackgroundResource(conditionCode)),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun WeatherBackgroundGradient() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.3f),
                        Color.Black.copy(alpha = 0.2f),
                    )
                )
            )
    )
}

@Composable
private fun WeatherContent(weather: CityWeatherDetailUiModel) {
    with(weather) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(96.dp))

            CurrentTemperatureSection(
                temperature = temperature,
                conditionCode = conditionCode,
                conditionText = conditionText,
                maxTemp = maxTemp,
                minTemp = minTemp
            )

            WeatherDetailsCard(
                humidity = humidity,
                pressureMb = pressureMb,
                windKph = windKph,
                visibilityKm = visibilityKm
            )

            ForecastCard(forecasts = forecasts)
        }
    }
}

@Composable
private fun WeatherTopBar(
    cityName: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        TopBar(cityName, onBack)
    }
}

@Composable
fun CurrentTemperatureSection(
    temperature: Int,
    conditionCode: Int,
    conditionText: String,
    maxTemp: Int,
    minTemp: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 48.dp)
    ) {
        TemperatureDisplay(temperature = temperature)
        WeatherConditionRow(
            conditionCode = conditionCode,
            conditionText = conditionText
        )
        TemperatureRange(maxTemp = maxTemp, minTemp = minTemp)
    }
}

@Composable
private fun TemperatureDisplay(temperature: Int) {
    Text(
        text = stringResource(R.string.temperature_format, temperature),
        fontSize = 120.sp,
        color = Color.White,
        fontWeight = FontWeight.Light
    )
}

@Composable
private fun WeatherConditionRow(
    conditionCode: Int,
    conditionText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(getIconResource(conditionCode)),
            contentDescription = stringResource(R.string.cd_weather_condition),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = conditionText,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
private fun TemperatureRange(maxTemp: Int, minTemp: Int) {
    Text(
        text = stringResource(R.string.max_min_temp_format, maxTemp, minTemp),
        fontSize = 18.sp,
        color = Color.White.copy(alpha = 0.9f)
    )
}

@Composable
fun WeatherDetailsCard(
    humidity: Int,
    pressureMb: Int,
    windKph: Int,
    visibilityKm: Int,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.padding(bottom = 24.dp)) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherDetailItem(
                    label = stringResource(R.string.label_humidity),
                    formattedValue = stringResource(R.string.format_percent, humidity),
                    rawValue = humidity,
                    modifier = Modifier.weight(1f)
                )
                WeatherDetailItem(
                    label = stringResource(R.string.label_air_pressure),
                    formattedValue = stringResource(R.string.format_hpa, pressureMb),
                    rawValue = pressureMb,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                WeatherDetailItem(
                    label = stringResource(R.string.label_wind),
                    formattedValue = stringResource(R.string.format_kmh, windKph),
                    rawValue = windKph,
                    modifier = Modifier.weight(1f)
                )
                WeatherDetailItem(
                    label = stringResource(R.string.label_visibility),
                    formattedValue = stringResource(R.string.format_km, visibilityKm),
                    rawValue = visibilityKm,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WeatherDetailItem(
    label: String,
    formattedValue: String,
    rawValue: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        if (rawValue > 0) {
            VerticalProgressBar(
                progress = rawValue / 100.0f,
                modifier = Modifier
                    .width(4.dp)
                    .padding(vertical = 4.dp)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = formattedValue,
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun ForecastCard(
    forecasts: List<ForecastDayUiModel>,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(modifier = Modifier.padding(24.dp)) {
            forecasts.forEachIndexed { index, day ->
                ForecastItem(
                    day = day,
                    showDivider = index < forecasts.size - 1
                )
            }
        }
    }
}

@Composable
fun ForecastItem(
    day: ForecastDayUiModel,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ForecastItemContent(day = day)
        if (showDivider) {
            ForecastDivider()
        }
    }
}

@Composable
private fun ForecastItemContent(day: ForecastDayUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ForecastDayLabel(dayOfWeek = day.dayOfWeek)
        ForecastWeatherIcon(conditionCode = day.conditionCode, modifier = Modifier.weight(1f))
        ForecastTemperatures(maxTemp = day.maxTempC, minTemp = day.minTempC)
    }
}

@Composable
private fun ForecastDayLabel(dayOfWeek: String) {
    Text(
        text = dayOfWeek,
        fontSize = 16.sp,
        color = Color.White,
        modifier = Modifier.width(112.dp)
    )
}

@Composable
private fun ForecastWeatherIcon(conditionCode: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(getIconResource(conditionCode)),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ForecastTemperatures(maxTemp: Int, minTemp: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.temperature_format, maxTemp),
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(R.string.temperature_format, minTemp),
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun ForecastDivider() {
    HorizontalDivider(
        color = Color.White.copy(alpha = 0.1f),
        thickness = 1.dp
    )
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PreviewWeatherDetailItem() {
    WeatherDetailItem(
        label = "Humidity",
        formattedValue = "65%",
        rawValue = 65
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PreviewForecastItem() {
    ForecastItem(
        day = ForecastDayUiModel(
            dayOfWeek = "Sunday",
            conditionCode = 1000,
            maxTempC = 22,
            minTempC = 15,
            conditionIcon = ""
        ),
        showDivider = true
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PreviewCurrentTemperatureSection() {
    CurrentTemperatureSection(
        temperature = 24,
        conditionCode = 1000,
        conditionText = "Sunny",
        maxTemp = 28,
        minTemp = 18
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PreviewWeatherDetailsCard() {
    WeatherDetailsCard(
        humidity = 65,
        pressureMb = 1013,
        windKph = 15,
        visibilityKm = 10
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PreviewForecastCard() {
    ForecastCard(
        forecasts = listOf(
            ForecastDayUiModel("Today", 25, -10, "", 1000),
            ForecastDayUiModel("Monday", 23, 2, "", 1003),
            ForecastDayUiModel("Tuesday", 21, 0, "", 1087)
        )
    )
}