package com.farshidabz.skysense.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.farshidabz.skysense.feature.detail.WeatherDetailRoute
import com.farshidabz.skysense.feature.home.HomeScreenRoute

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Route.Home)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = { key: NavKey ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        HomeScreenRoute(
                            onCityClick = { cityName ->
                                backStack.add(Route.WeatherDetail(cityName))
                            }
                        )
                    }
                }

                is Route.WeatherDetail -> {
                    NavEntry(key) {
                        WeatherDetailRoute(cityName = key.cityName, onBack = {
                            backStack.removeLastOrNull()
                        })
                    }
                }

                else -> error("Unknown NavKey: $key")
            }
        }
    )
}