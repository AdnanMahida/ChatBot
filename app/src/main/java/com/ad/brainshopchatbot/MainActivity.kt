package com.ad.brainshopchatbot

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.ad.brainshopchatbot.adapter.MessageAdapter
import com.ad.brainshopchatbot.databinding.ActivityMainBinding
import com.ad.brainshopchatbot.model.Message
import com.ad.brainshopchatbot.network.ResultState
import com.ad.brainshopchatbot.repository.BaseRepository.Companion.getErrorMessage
import com.ad.brainshopchatbot.util.Formatter
import com.ad.brainshopchatbot.util.showAlertDialog
import com.ad.brainshopchatbot.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val chatViewModel: ChatViewModel by viewModels()

    private val messageAdapter: MessageAdapter = MessageAdapter()

    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.initUI()
        observer()
    }


    private fun ActivityMainBinding.initUI() {
        supportActionBar?.title = getString(R.string.chat_bot)
        rvMessages.layoutManager = LinearLayoutManager(this@MainActivity)
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
                chatViewModel.getBotAnswer(Formatter.formatMessage(message))
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
}