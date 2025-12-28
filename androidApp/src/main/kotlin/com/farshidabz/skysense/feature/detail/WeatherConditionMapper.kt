package com.farshidabz.skysense.feature.detail

import com.farshidabz.skysense.R

object WeatherConditionMapper {
    fun getIconResource(code: Int): Int = when (code) {
        in SUNNY -> R.drawable.ic_sunny
        in CLOUDY -> R.drawable.ic_clouds
        in FOGGY -> R.drawable.ic_foggy
        in RAINY -> R.drawable.ic_rain
        in HEAVY_RAIN -> R.drawable.ic_heavy_rain
        in THUNDERSTORM -> R.drawable.ic_thunderstorm
        in SNOWY -> R.drawable.ic_snowy
        in FREEZING -> R.drawable.ic_freezing_rain
        else -> R.drawable.ic_sunny
    }

    fun getBackgroundResource(code: Int): Int = when (code) {
        in SUNNY -> R.drawable.bg_sun
        in CLOUDY -> R.drawable.bg_light_clouds
        in FOGGY -> R.drawable.bg_fog
        in RAINY -> R.drawable.bg_rain
        in HEAVY_RAIN -> R.drawable.bg_heavy_clouds
        in THUNDERSTORM -> R.drawable.bg_thunder
        in SNOWY -> R.drawable.bg_snow
        in FREEZING -> R.drawable.freezing
        else -> R.drawable.bg_sun
    }

    private val SUNNY = setOf(1000)
    private val CLOUDY = setOf(1003, 1006, 1009)
    private val FOGGY = setOf(1030, 1135, 1147)
    private val RAINY = setOf(1063, 1180, 1183, 1186, 1189, 1192, 1195, 1240, 1243)
    private val HEAVY_RAIN = setOf(1198, 1201, 1246)
    private val THUNDERSTORM = setOf(1087, 1273, 1276, 1279, 1282)
    private val SNOWY = setOf(
        1066, 1069, 1072, 1114, 1117, 1204, 1207,
        1210, 1213, 1216, 1219, 1222, 1225,
        1237, 1249, 1252, 1255, 1258, 1261, 1264
    )
    private val FREEZING = setOf(1150, 1153, 1168, 1171)
}