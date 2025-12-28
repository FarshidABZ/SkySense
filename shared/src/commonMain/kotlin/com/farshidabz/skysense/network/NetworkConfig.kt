package com.farshidabz.skysense.network

import com.farshidabz.skysense.AppConfig

/**
 * Basic network configuration shared across platforms.
 */
data class NetworkConfig(
    val baseUrl: String = AppConfig.BASE_URL,
    val apiKey: String? = AppConfig.API_KEY,
    val connectTimeoutMillis: Long = 15_000,
    val socketTimeoutMillis: Long = 30_000,
)
