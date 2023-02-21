package com.ad.brainshopchatbot.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.brainshopchatbot.model.BotResponse
import com.ad.brainshopchatbot.network.ResultState
import com.ad.brainshopchatbot.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val botAnswerLiveData = MutableLiveData<ResultState<BotResponse>>()

    fun getBotAnswer(
        message: String,
    ) {
        viewModelScope.launch {
            val response = repository.getBotAnswer(message = message)
            response.let {
                when (it) {
                    is ResultState.Success -> it.extractData?.let { list ->
                        botAnswerLiveData.postValue(ResultState.Success(list))
                    }
                    else -> botAnswerLiveData.postValue(it)
                }
            }
        }
    }
}