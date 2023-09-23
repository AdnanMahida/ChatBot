package com.ad.brainshopchatbot.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.brainshopchatbot.model.BotResponse
import com.ad.brainshopchatbot.network.ResultState
import com.ad.brainshopchatbot.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {
    val botAnswerLiveData = MutableLiveData<ResultState<BotResponse>>()

    fun getBotAnswer(message: String) {
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