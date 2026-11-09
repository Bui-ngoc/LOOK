package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LookRepository
import com.example.model.MemoryChatMessage
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkCard
import com.example.ui.theme.LookDarkSurface
import com.example.ui.theme.LookGold
import com.example.ui.theme.LookNeonCyan
import com.example.ui.theme.LookPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AiCompanionScreen(
  modifier: Modifier = Modifier
) {
  val chatMessages by LookRepository.chatMessages.collectAsState()
  var inputQuery by remember { mutableStateOf("") }

  val suggestionPrompts = listOf(
    "Ôn lại kỷ niệm Hồ Tây với Quân 🌅",
    "Gợi ý caption thơ mộng chiều nay ✨",
    "Soulbound Token (SBT) hoạt động thế nào?",
    "Hôm nay có kỷ niệm nào cũ không?"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(LookDarkSurface)
        .border(1.dp, LookBorder, RoundedCornerShape(20.dp))
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(LookGold.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = LookGold,
          modifier = Modifier.size(24.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Look AI Memory Companion",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        )
        Text(
          text = "Trợ lý hồi tưởng & sáng tạo khoảnh khắc",
          style = MaterialTheme.typography.bodySmall.copy(color = LookNeonCyan, fontSize = 11.sp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Quick suggestion prompt chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(suggestionPrompts) { prompt ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(LookDarkCard)
            .border(1.dp, LookBorder, RoundedCornerShape(14.dp))
            .clickable { LookRepository.sendChatMessage(prompt) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(text = prompt, fontSize = 11.sp, color = TextPrimary)
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Chat Messages List
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(vertical = 8.dp)
    ) {
      items(chatMessages, key = { it.id }) { msg ->
        ChatMessageBubble(msg)
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Message Input
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = inputQuery,
        onValueChange = { inputQuery = it },
        modifier = Modifier.weight(1f),
        placeholder = { Text("Hỏi Look AI về kỷ niệm hoặc ảnh...", fontSize = 13.sp, color = TextSecondary) },
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = LookDarkCard,
          unfocusedContainerColor = LookDarkCard,
          focusedBorderColor = LookGold,
          unfocusedBorderColor = LookBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        textStyle = MaterialTheme.typography.bodyMedium
      )
      Spacer(modifier = Modifier.width(8.dp))
      IconButton(
        onClick = {
          if (inputQuery.isNotBlank()) {
            LookRepository.sendChatMessage(inputQuery)
            inputQuery = ""
          }
        },
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(LookGold)
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "Send",
          tint = Color.Black
        )
      }
    }
  }
}

@Composable
fun ChatMessageBubble(msg: MemoryChatMessage) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (msg.isUser) Alignment.End else Alignment.Start
  ) {
    Box(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 18.dp,
            bottomStart = if (msg.isUser) 18.dp else 4.dp,
            bottomEnd = if (msg.isUser) 4.dp else 18.dp
          )
        )
        .background(if (msg.isUser) LookGold else LookDarkSurface)
        .border(
          width = 1.dp,
          color = if (msg.isUser) LookGold else LookBorder,
          shape = RoundedCornerShape(18.dp)
        )
        .padding(14.dp)
        .widthIn(max = 290.dp)
    ) {
      Column {
        Text(
          text = msg.text,
          color = if (msg.isUser) Color.Black else TextPrimary,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = if (msg.isUser) FontWeight.SemiBold else FontWeight.Normal
          )
        )

        if (msg.relatedMomentImage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16f / 10f)
              .clip(RoundedCornerShape(12.dp))
              .background(LookDarkCard)
          ) {
            AsyncImage(
              model = msg.relatedMomentImage,
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }
        }
      }
    }
  }
}

