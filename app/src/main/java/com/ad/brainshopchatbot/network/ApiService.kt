package com.ad.brainshopchatbot.network


import com.ad.brainshopchatbot.model.BotResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("get")
    suspend fun getBotAnswer(
        @Query("bid") bid: String,
        @Query("key") key: String,
        @Query("uid") udi: String,
        @Query("msg") msg: String,
    ): Response<BotResponse>?
}