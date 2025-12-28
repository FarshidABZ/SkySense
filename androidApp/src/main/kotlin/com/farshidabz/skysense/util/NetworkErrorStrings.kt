package com.farshidabz.skysense.util

import androidx.annotation.StringRes
import com.farshidabz.skysense.R
import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.network.NetworkError

@StringRes
fun NetworkError.asStringRes(): Int = when (this) {
    NetworkError.NoInternet -> R.string.error_no_internet
    NetworkError.Timeout -> R.string.error_timeout
    NetworkError.SSLHandshake -> R.string.error_ssl_handshake
    NetworkError.Cancelled -> R.string.error_cancelled
    NetworkError.Unauthorized -> R.string.error_unauthorized
    NetworkError.Forbidden -> R.string.error_forbidden
    NetworkError.NotFound -> R.string.error_not_found
    NetworkError.TooManyRequests -> R.string.error_too_many_requests
    NetworkError.ClientError -> R.string.error_client
    NetworkError.ServerUnavailable -> R.string.error_server_unavailable
    NetworkError.ServerError -> R.string.error_server
    NetworkError.Serialization -> R.string.error_serialization
    NetworkError.Unknown -> R.string.error_unknown
}

@StringRes
fun AppError.asStringRes(): Int = when (this) {
    is AppError.Network -> this.kind.asStringRes()
    AppError.Serialization -> R.string.error_serialization
    AppError.Unknown -> R.string.error_unknown
}