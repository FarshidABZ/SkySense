package com.farshidabz.skysense.network

import com.farshidabz.skysense.base.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlinx.coroutines.CancellationException

enum class NetworkError {
    NoInternet,
    Timeout,
    SSLHandshake,
    Cancelled,
    // HTTP level
    Unauthorized,            // 401
    Forbidden,               // 403
    NotFound,                // 404
    TooManyRequests,         // 429
    ClientError,             // 4xx (other)
    ServerUnavailable,       // 503/504
    ServerError,             // 5xx (other)
    // Data / parsing
    Serialization,
    Unknown,
}

fun Throwable.toNetworkError(): NetworkError {
    if (this is CancellationException) return NetworkError.Cancelled

    if (this is HttpRequestTimeoutException) return NetworkError.Timeout

    if (this is IOException) {
        val msg = message?.lowercase().orEmpty()
        return when {
            "unable to resolve host" in msg ||
            "no address associated with hostname" in msg ||
            "unresolvedaddress" in msg ||
            "host is down" in msg ||
            "network is unreachable" in msg ||
            "connection refused" in msg ||
            "offline" in msg -> NetworkError.NoInternet

            "ssl" in msg || "certificate" in msg || "handshake" in msg -> NetworkError.SSLHandshake

            "timeout" in msg -> NetworkError.Timeout

            else -> NetworkError.NoInternet
        }
    }

    val msg = message?.lowercase().orEmpty()
    if ("json" in msg || "serialization" in msg || "unexpected character" in msg) {
        return NetworkError.Serialization
    }

    return NetworkError.Unknown
}

fun HttpResponse.toNetworkError(): NetworkError {
    if (status.isSuccess()) return NetworkError.Unknown
    val code = status.value
    return when (code) {
        401 -> NetworkError.Unauthorized
        403 -> NetworkError.Forbidden
        404 -> NetworkError.NotFound
        429 -> NetworkError.TooManyRequests
        503, 504 -> NetworkError.ServerUnavailable
        in 400..499 -> NetworkError.ClientError
        in 500..599 -> NetworkError.ServerError
        else -> NetworkError.Unknown
    }
}