package com.c241ps319.patera.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResultState<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultState<T>()
    data class Error(val error: String?) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            Loading -> "Loading"
        }
    }
}