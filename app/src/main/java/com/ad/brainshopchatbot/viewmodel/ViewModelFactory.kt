package com.ad.brainshopchatbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ad.brainshopchatbot.network.ApiService
import com.ad.brainshopchatbot.repository.ChatRepository

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(ChatRepository(apiService)) as T
        } else throw IllegalArgumentException("Unknown class name")
    }

}