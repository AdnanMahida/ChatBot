package com.ad.brainshopchatbot.repository

import com.ad.brainshopchatbot.network.ResultState


abstract class BaseRepository {
    companion object {
        private const val NOT_FOUND = 404
        private const val ALREADY_EXIST = 409
        const val UNAUTHORIZED = 401
        const val BAD_REQUEST = 400
        const val GENERAL_ERROR_CODE = 499
        const val GENERAL_ERROR_MESSAGE = "connection closed"
        const val INVALID_TOKEN = "Invalid token"


        fun <T : Any> handleSuccess(data: T): ResultState<T> {
            return ResultState.Success(data)
        }

        fun <T : Any> handleException(code: Int, errorMessage: String): ResultState<T> {
            return ResultState.Error(code, errorMessage)
        }

        fun getErrorMessage(httpCode: Int): String {
            return when (httpCode) {
                BAD_REQUEST -> "Bad Request"
                UNAUTHORIZED -> "Unauthorized"
                NOT_FOUND -> "Not found"
                ALREADY_EXIST -> "Already exist"
                else -> "Something went wrong"
            }
        }
    }
}