package com.farshidabz.skysense.base

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val error: AppError,
        val cause: Throwable? = null,
    ) : Result<Nothing>()
}