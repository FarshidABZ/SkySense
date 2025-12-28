package com.farshidabz.skysense.base

import com.farshidabz.skysense.network.NetworkError

sealed interface AppError {
    data class Network(val kind: NetworkError) : AppError
    data object Serialization : AppError
    data object Unknown : AppError
}
