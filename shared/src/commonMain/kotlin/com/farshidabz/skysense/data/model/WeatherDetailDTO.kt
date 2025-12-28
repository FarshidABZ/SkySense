package com.farshidabz.skysense.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetailDTO(
    val location: LocationDTO,
    val current: CurrentWeatherDTO,
    val forecast: ForecastDTO
)

@Serializable
data class CurrentWeatherDTO(
    @SerialName("last_updated_epoch")
    val lastUpdatedEpoch: Long,
    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("temp_c")
    val tempC: Float,
    @SerialName("temp_f")
    val tempF: Float,
    @SerialName("is_day")
    val isDay: Int,
    val condition: ConditionDTO,
    @SerialName("wind_mph")
    val windMph: Float,
    @SerialName("wind_kph")
    val windKph: Float,
    @SerialName("wind_degree")
    val windDegree: Int,
    @SerialName("wind_dir")
    val windDir: String,
    @SerialName("pressure_mb")
    val pressureMb: Float,
    @SerialName("pressure_in")
    val pressureIn: Float,
    @SerialName("precip_mm")
    val precipMm: Float,
    @SerialName("precip_in")
    val precipIn: Float,
    val humidity: Int,
    val cloud: Int,
    @SerialName("feelslike_c")
    val feelslikeC: Float,
    @SerialName("feelslike_f")
    val feelslikeF: Float,
    @SerialName("vis_km")
    val visKm: Float,
    @SerialName("vis_miles")
    val visMiles: Float,
    val uv: Float,
    @SerialName("gust_mph")
    val gustMph: Float,
    @SerialName("gust_kph")
    val gustKph: Float
)

@Serializable
data class ForecastDTO(
    val forecastday: List<ForecastDayDTO>
)

@Serializable
data class ForecastDayDTO(
    val date: String,
    @SerialName("date_epoch")
    val dateEpoch: Long,
    val day: DayDTO,
)

@Serializable
data class DayDTO(
    @SerialName("maxtemp_c")
    val maxtempC: Float,
    @SerialName("maxtemp_f")
    val maxtempF: Float,
    @SerialName("mintemp_c")
    val mintempC: Float,
    @SerialName("mintemp_f")
    val mintempF: Float,
    @SerialName("avgtemp_c")
    val avgtempC: Float,
    @SerialName("avgtemp_f")
    val avgtempF: Float,
    @SerialName("maxwind_mph")
    val maxwindMph: Float,
    @SerialName("maxwind_kph")
    val maxwindKph: Float,
    @SerialName("totalprecip_mm")
    val totalprecipMm: Float,
    @SerialName("totalprecip_in")
    val totalprecipIn: Float,
    @SerialName("avgvis_km")
    val avgvisKm: Float,
    @SerialName("avgvis_miles")
    val avgvisMiles: Float,
    val avghumidity: Int,
    @SerialName("daily_will_it_rain")
    val dailyWillItRain: Int,
    @SerialName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @SerialName("daily_will_it_snow")
    val dailyWillItSnow: Int,
    @SerialName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    val condition: ConditionDTO,
    val uv: Float
)