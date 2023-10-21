package com.interview.dragonflights.ui.utils

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    fun <T> Result<T>.successOr(fallback: T): T {
        return (this as? Success<T>)?.data ?: fallback
    }
}