package com.ad.brainshopchatbot

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ad.brainshopchatbot.model.Message
import com.ad.brainshopchatbot.network.ResultState
import com.ad.brainshopchatbot.repository.BaseRepository
import com.ad.brainshopchatbot.ui.theme.BgColor
import com.ad.brainshopchatbot.ui.theme.BrainShopChatBotTheme
import com.ad.brainshopchatbot.ui.theme.Gray
import com.ad.brainshopchatbot.ui.theme.PurpleGrey80
import com.ad.brainshopchatbot.util.Formatter
import com.ad.brainshopchatbot.util.ShowAlertDialog
import com.ad.brainshopchatbot.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val chatViewModel: ChatViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            BrainShopChatBotTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val messageList = remember { mutableStateListOf<Message>() }
                    var msg by remember { mutableStateOf("") }
                    var errorMsg by remember { mutableStateOf("") }
                    var showErrorDialog by remember { mutableStateOf(false) }


                    if (showErrorDialog) {
                        ShowAlertDialog(
                            onDismissRequest = { showErrorDialog = false },
                            onConfirmation = {
                                showErrorDialog = false
                            },
                            dialogTitle = context.getString(R.string.app_name),
                            dialogText = errorMsg
                        )
                    }

                    LaunchedEffect(chatViewModel.botAnswerLiveData.observeAsState().value) {
                        when (val list = chatViewModel.botAnswerLiveData.value) {
                            is ResultState.Success -> {
                                list.data.answer?.let { message ->
                                    messageList.add(Message(isFromBot = true, message))
                                }
                            }

                            is ResultState.InProgress -> {}
                            is ResultState.Error -> {
                                showErrorDialog = true
                                errorMsg =
                                    "${BaseRepository.getErrorMessage(list.errorCode)} ${list.errorMessage}"
                            }

                            else -> {}
                        }
                    }

                    Scaffold(topBar = {
                        TopAppBar(colors = topAppBarColors(
                            containerColor = PurpleGrey80,
                            titleContentColor = Color.White,
                        ), title = {
                            Text(context.getString(R.string.app_name))
                        })
                    }, bottomBar = {
                        BottomAppBar(
                            containerColor = BgColor,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .background(color = Color.White)
                                        .weight(1f),
                                    value = msg,
                                    onValueChange = { msg = it },
                                    placeholder = {
                                        Text(text = context.getString(R.string.type_a_message))
                                    },
                                    shape = RoundedCornerShape(20.dp)

                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                FloatingActionButton(
                                    shape = CircleShape, onClick = {
                                        if (msg.isNotEmpty()) {
                                            messageList.add(Message(isFromBot = false, msg))
                                            chatViewModel.getBotAnswer(Formatter.formatMessage(msg))
                                            msg = ""
                                        }
                                    }, containerColor = PurpleGrey80
                                ) {
                                    Icon(
                                        Icons.Filled.Send,
                                        "Floating action button.",
                                        tint = Color.White
                                    )
                                }
                            }

                        }
                    }) { innerPadding ->

                        Column(
                            modifier = Modifier.padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            LazyColumn(Modifier.fillMaxSize()) {
                                items(messageList.size) {
                                    ChatMessage(messageList[it])
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessage(
    message: Message
) {
    val isBotMessage = message.isFromBot

    val backgroundColor = if (isBotMessage) Gray else PurpleGrey80
    val shape = RoundedCornerShape(
        topStart = if (isBotMessage) 0.dp else 20.dp,
        topEnd = 20.dp,
        bottomStart = 20.dp,
        bottomEnd = if (isBotMessage) 20.dp else 0.dp
    )
    val alignment = if (isBotMessage) Alignment.TopStart else Alignment.TopEnd

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor, shape = shape)
                .width(200.dp)
                .padding(14.dp)
        ) {
            Text(
                text = message.message, color = Color.White, fontSize = 14.sp
            )
        }
    }
}