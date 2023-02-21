package com.ad.brainshopchatbot.model


import com.google.gson.annotations.SerializedName

data class BotResponse(
    @SerializedName("cnt") val answer: String? = null
)