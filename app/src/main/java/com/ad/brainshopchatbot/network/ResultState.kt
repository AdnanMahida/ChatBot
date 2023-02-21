package com.ad.brainshopchatbot.network

sealed class ResultState<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultState<T>()

    data class Error(val errorCode: Int, val errorMessage: String) : ResultState<Nothing>()

    object InProgress : ResultState<Nothing>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
        }
}