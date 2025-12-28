package com.farshidabz.skysense.network

import io.ktor.client.HttpClient

/**
 * Expect/Actual provider to build a platform HttpClient with shared config.
 */
expect fun provideHttpClient(config: NetworkConfig): HttpClient
