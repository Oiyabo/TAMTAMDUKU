package com.example.tamtamduku.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.data.model.ChatMessage
import com.example.tamtamduku.ui.viewmodels.ChatViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalChat(
    userName: String,
    onBack: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState()
    val chat = remember(chats, userName) {
        chats.find { it.name == userName }
    }
    
    val bgColor = Color(0xFFFFFDFB)
    val primaryOrange = Color(0xFFFF7B00)
    val yellowBubble = Color(0xFFFFD600)

    var messageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp).clip(CircleShape),
                            color = Color.DarkGray
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (userName.isNotEmpty()) userName.take(1) else "?",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = userName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Online",
                                color = Color(0xFF4CAF50),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.Undo, 
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor,
        bottomBar = {
            ChatInput(
                value = messageText,
                onValueChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(userName, messageText)
                        messageText = ""
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            val isLoading by viewModel.isLoading.collectAsState()
            if (isLoading && chat == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val messages = chat?.roomId?.let { viewModel.getMessagesForRoom(it) } ?: emptyList()
                val listState = rememberLazyListState()
                
                LaunchedEffect(messages.size) {
                    if (messages.isNotEmpty()) {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(messages) { message ->
                        MessageBubble(message, primaryOrange, yellowBubble)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage, myColor: Color, otherColor: Color) {
    val isFromMe = message.senderId.startsWith("usr_")
    val alignment = if (isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (isFromMe) myColor else otherColor

    // Format time dynamically
    val formattedTime = try {
        val dt = ZonedDateTime.parse(message.time)
        val now = ZonedDateTime.now(dt.zone)
        if (dt.toLocalDate() == now.toLocalDate()) {
            dt.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            dt.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"))
        }
    } catch (e: Exception) {
        message.time
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Surface(
            color = bgColor,
            // Small rounded corner
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = message.text,
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
                Text(
                    text = formattedTime,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        color = Color(0xFFFFE4C4), // pastel orange/peach
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 50.dp, max = 100.dp),
                placeholder = { Text("Ketik pesan...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFF7B00), CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send, 
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
