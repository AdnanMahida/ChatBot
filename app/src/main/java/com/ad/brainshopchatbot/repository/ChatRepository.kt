package com.ad.brainshopchatbot.repository

import com.ad.brainshopchatbot.BuildConfig
import com.ad.brainshopchatbot.model.BotResponse
import com.ad.brainshopchatbot.network.ApiService
import com.ad.brainshopchatbot.network.ResultState
import org.json.JSONObject
import retrofit2.HttpException

class ChatRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getBotAnswer(
        message: String,
    ): ResultState<BotResponse> {
//        handleLoading()
        try {
            val response = apiService.getBotAnswer(
                bid = BuildConfig.BRAIN_ID,
                key = BuildConfig.API_KEY,
                udi = "1",
                msg = message
            )
            response?.let {
                it.body()?.let { res ->
                    return handleSuccess(res)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.let { res ->
                            return handleException(res.code(), res.message())
                        }
                    }
                    if (response.code() == BAD_REQUEST) {
                        val errorJsonObj = JSONObject(responseErrorBody.string())
                        return handleException(response.code(), errorJsonObj.toString())
                    }
                }
            }
            return handleException(
                response?.code() ?: GENERAL_ERROR_CODE,
                response?.message() ?: GENERAL_ERROR_MESSAGE
            )

        } catch (error: HttpException) {
            return handleException(error.code(), error.message())
        } catch (error: Exception) {
            return handleException(
                GENERAL_ERROR_CODE,
                error.message ?: GENERAL_ERROR_MESSAGE
            )
        }
    }

}