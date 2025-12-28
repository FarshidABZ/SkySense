package com.farshidabz.skysense.network

import com.farshidabz.skysense.base.AppError
import com.farshidabz.skysense.base.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> HttpResponse.toNetworkResult(): Result<T> {
    return try {
        if (status.isSuccess()) {
            Result.Success(this.body())
        } else {
            val category = this.toNetworkError()
            val appError = when (category) {
                NetworkError.Serialization -> AppError.Serialization
                else -> AppError.Network(category)
            }
            Result.Error(
                error = appError
            )
        }
    } catch (ce: CancellationException) {
        throw ce
    } catch (t: Throwable) {
        val category = t.toNetworkError()
        val appError = when (category) {
            NetworkError.Serialization -> AppError.Serialization
            else -> AppError.Network(category)
        }
        Result.Error(error = appError, cause = t)
    }
}