package com.ad.brainshopchatbot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ad.brainshopchatbot.adapter.MessageAdapter
import com.ad.brainshopchatbot.databinding.ActivityMainBinding
import com.ad.brainshopchatbot.model.Message
import com.ad.brainshopchatbot.network.ApiService
import com.ad.brainshopchatbot.network.ResultState
import com.ad.brainshopchatbot.network.RetrofitService
import com.ad.brainshopchatbot.repository.BaseRepository.Companion.getErrorMessage
import com.ad.brainshopchatbot.util.Formatter.formatMessage
import com.ad.brainshopchatbot.util.showAlertDialog
import com.ad.brainshopchatbot.viewmodel.ChatViewModel
import com.ad.brainshopchatbot.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var chatViewModel: ChatViewModel

    private lateinit var messageAdapter: MessageAdapter

    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        binding.initUI()
        observer()
    }


    private fun ActivityMainBinding.initUI() {
        supportActionBar?.title = getString(R.string.chat_bot)
        messageAdapter = MessageAdapter()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@MainActivity)
        rvMessages.layoutManager = layoutManager
        rvMessages.adapter = messageAdapter
        messageAdapter.submitList(messageList)

        btnSend.setOnClickListener {
            val message = etMessage.text.trim().toString()
            if (message.isNotEmpty()) {
                messageList.add(Message(isFromBot = false, message))
                val position = messageList.size - 1
                messageAdapter.notifyItemInserted(position)
                rvMessages.scrollToPosition(position)
                etMessage.setText("")
                val formattedMessage = formatMessage(message)
                chatViewModel.getBotAnswer(formattedMessage)
            }
        }
    }

    private fun observer() {
        chatViewModel.botAnswerLiveData.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    it.data.answer?.let { message ->
                        messageList.add(Message(isFromBot = true, message))
                        val position = messageList.size - 1
                        messageAdapter.notifyItemInserted(position)
                        binding.rvMessages.scrollToPosition(position)
                    }
                }
                is ResultState.InProgress -> {
                }

                is ResultState.Error -> {
                    showAlertDialog("${getErrorMessage(it.errorCode)} ${it.errorMessage}") { dialogInterface ->
                        dialogInterface.dismiss()
                    }
                }

            }
        }
    }

    private fun initViewModel() {
        if ((::chatViewModel.isInitialized).not()) {
            chatViewModel = ViewModelProvider(
                this@MainActivity,
                ViewModelFactory(RetrofitService.createService(ApiService::class.java))
            )[ChatViewModel::class.java]
        }
    }
}