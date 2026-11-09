package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LookRepository
import com.example.model.AudienceVisibility
import com.example.model.LookMoment
import com.example.model.LookThought
import com.example.ui.components.LookWidgetPreview
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkCard
import com.example.ui.theme.LookDarkSurface
import com.example.ui.theme.LookGold
import com.example.ui.theme.LookNeonCyan
import com.example.ui.theme.LookPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FeedScreen(
  onNavigateToCapture: () -> Unit,
  onNavigateToChat: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val moments by LookRepository.moments.collectAsState()
  val activeWidgetMoment by LookRepository.activeWidgetMoment.collectAsState()
  val thoughts by LookRepository.thoughts.collectAsState()
  val streakCount by LookRepository.currentStreak.collectAsState()

  var showThoughtDialog by remember { mutableStateOf(false) }
  var newThoughtText by remember { mutableStateOf("") }
  var newThoughtEmoji by remember { mutableStateOf("⚡") }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Streak & Header Banner
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFF331600), Color(0xFF1B1428))
            )
          )
          .border(1.dp, LookGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Color(0xFFFF6D00).copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak",
              tint = Color(0xFFFF6D00),
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "$streakCount NGÀY STREAK LIÊN TIẾP 🔥",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LookGold
              )
            )
            Text(
              text = "Bạn thân nhất: Minh Quân (7 ngày)",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LookGold)
            .clickable { onNavigateToCapture() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text("Giữ Streak", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
      }
    }

    // 2. Short Posts / Thoughts Horizontal Carousel
    item {
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Trạng thái ngắn (Thoughts)",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Text(
            text = "+ Đăng trạng thái",
            color = LookNeonCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { showThoughtDialog = true }
          )
        }

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Add Thought Button card
          item {
            Box(
              modifier = Modifier
                .size(width = 110.dp, height = 120.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(LookDarkSurface)
                .border(1.dp, LookBorder, RoundedCornerShape(18.dp))
                .clickable { showThoughtDialog = true }
                .padding(10.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(LookGold.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Add, contentDescription = null, tint = LookGold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Chia sẻ suy nghĩ", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
              }
            }
          }

          items(thoughts, key = { it.id }) { thought ->
            ThoughtCard(thought)
          }
        }
      }
    }

    // 3. Live Widget Simulation Header
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(LookDarkSurface)
          .border(1.dp, LookBorder, RoundedCornerShape(24.dp))
          .padding(16.dp)
      ) {
        LookWidgetPreview(
          moment = activeWidgetMoment,
          isHomeScreenSimulator = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Ảnh hiển thị trên màn hình chính của bạn",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
          )
          Button(
            onClick = onNavigateToCapture,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = LookGold,
              contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text("Chụp ảnh mới", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }

    // Header for Timeline
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Khoảnh khắc bạn bè (${moments.size})",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        )
        Text(
          text = "Realtime Sync",
          style = MaterialTheme.typography.labelSmall.copy(color = LookNeonCyan)
        )
      }
    }

    // Moments List
    items(moments, key = { it.id }) { moment ->
      MomentCardItem(
        moment = moment,
        isActiveOnWidget = moment.id == activeWidgetMoment.id,
        onSetAsWidget = {
          LookRepository.setWidgetMoment(moment)
          Toast.makeText(context, "Đã ghim ảnh của ${moment.senderName} lên Widget!", Toast.LENGTH_SHORT).show()
        },
        onToggleLike = {
          LookRepository.toggleLike(moment.id)
        },
        onMintSbt = {
          LookRepository.mintExistingMoment(moment.id)
          Toast.makeText(context, "Đã đúc Soulbound Token vĩnh cửu trên Polygon!", Toast.LENGTH_LONG).show()
        },
        onOpenChat = onNavigateToChat
      )
    }
  }

  // Thought Dialog
  if (showThoughtDialog) {
    AlertDialog(
      onDismissRequest = { showThoughtDialog = false },
      containerColor = LookDarkSurface,
      title = {
        Text("Chia sẻ suy nghĩ ngắn", fontWeight = FontWeight.Bold, color = TextPrimary)
      },
      text = {
        Column {
          OutlinedTextField(
            value = newThoughtText,
            onValueChange = { newThoughtText = it },
            placeholder = { Text("Bạn đang nghĩ gì? (tối đa 60 ký tự)", color = TextSecondary, fontSize = 12.sp) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = LookDarkCard,
              unfocusedContainerColor = LookDarkCard,
              focusedBorderColor = LookGold,
              unfocusedBorderColor = LookBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("⚡", "☕", "💻", "🔥", "🌸", "🍕").forEach { em ->
              Text(
                text = em,
                fontSize = 20.sp,
                modifier = Modifier
                  .clickable { newThoughtEmoji = em }
                  .background(if (newThoughtEmoji == em) LookGold.copy(alpha = 0.2f) else Color.Transparent, CircleShape)
                  .padding(4.dp)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newThoughtText.isNotBlank()) {
              LookRepository.addNewThought(newThoughtText, newThoughtEmoji)
              newThoughtText = ""
              showThoughtDialog = false
              Toast.makeText(context, "Đã đăng suy nghĩ!", Toast.LENGTH_SHORT).show()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = LookGold, contentColor = Color.Black)
        ) {
          Text("Đăng ngay", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showThoughtDialog = false }) {
          Text("Hủy", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
fun ThoughtCard(thought: LookThought) {
  Box(
    modifier = Modifier
      .size(width = 140.dp, height = 120.dp)
      .clip(RoundedCornerShape(18.dp))
      .background(
        Brush.linearGradient(
          colors = listOf(
            LookDarkCard,
            LookDarkSurface
          )
        )
      )
      .border(1.dp, LookBorder, RoundedCornerShape(18.dp))
      .padding(10.dp)
  ) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
          model = thought.authorAvatar,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = thought.authorName,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
          maxLines = 1
        )
      }

      Text(
        text = "${thought.emoji} ${thought.text}",
        fontSize = 11.sp,
        color = TextPrimary,
        fontWeight = FontWeight.Medium,
        maxLines = 3
      )

      Text(
        text = thought.timeAgo,
        fontSize = 9.sp,
        color = TextSecondary
      )
    }
  }
}

@Composable
fun MomentCardItem(
  moment: LookMoment,
  isActiveOnWidget: Boolean,
  onSetAsWidget: () -> Unit,
  onToggleLike: () -> Unit,
  onMintSbt: () -> Unit,
  onOpenChat: () -> Unit
) {
  var commentInput by remember { mutableStateOf("") }
  val context = LocalContext.current

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(LookDarkSurface)
      .border(
        width = if (isActiveOnWidget) 2.dp else 1.dp,
        color = if (isActiveOnWidget) LookGold else LookBorder,
        shape = RoundedCornerShape(24.dp)
      )
      .padding(14.dp)
  ) {
    // Top Sender info & Audience badge
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
          model = moment.senderAvatar,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .border(1.5.dp, LookGold, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = moment.senderName,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(LookDarkCard)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (moment.audience == AudienceVisibility.CLOSE_FRIENDS) "Bạn thân" else "Công khai",
                fontSize = 9.sp,
                color = LookNeonCyan
              )
            }
          }
          Text(
            text = moment.timeAgo,
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
          )
        }
      }

      if (isActiveOnWidget) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LookGold.copy(alpha = 0.2f))
            .border(1.dp, LookGold, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text("ĐANG TRÊN WIDGET", color = LookGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      } else {
        IconButton(
          onClick = onSetAsWidget,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PhoneAndroid,
            contentDescription = "Set to Widget",
            tint = TextSecondary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Moment Photo
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(18.dp))
        .background(LookDarkCard)
    ) {
      AsyncImage(
        model = moment.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      if (moment.blockchainMetadata != null) {
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LookPurple.copy(alpha = 0.9f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "SBT #${moment.blockchainMetadata.tokenId.takeLast(4)}",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Tagged Friends
    if (moment.taggedFriends.isNotEmpty()) {
      Row(
        modifier = Modifier.padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("cùng với ", fontSize = 11.sp, color = TextSecondary)
        moment.taggedFriends.forEach { tag ->
          Text(
            text = "@$tag ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = LookGold
          )
        }
      }
    }

    // AI Caption & Mood
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = LookGold, modifier = Modifier.size(14.dp))
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "Mood: ${moment.aiMetadata.mood}",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = LookGold
      )
    }

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = moment.aiMetadata.caption,
      style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Reactions & Open Chat Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
          modifier = Modifier
            .clickable { onToggleLike() }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (moment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            tint = if (moment.isLiked) Color(0xFFFF4B6E) else TextSecondary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${moment.reactionsCount}",
            color = TextSecondary,
            fontSize = 12.sp
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Open Direct Chat Button
        Row(
          modifier = Modifier
            .clickable { onOpenChat() }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = "Chat",
            tint = LookNeonCyan,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Nhắn tin riêng",
            color = LookNeonCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      if (moment.blockchainMetadata == null) {
        Button(
          onClick = onMintSbt,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = LookPurple.copy(alpha = 0.2f),
            contentColor = LookPurple
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, LookPurple),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Đúc SBT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      } else {
        Text(
          text = "Polygon SBT",
          color = LookPurple,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Quick Reply to Moment
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(LookDarkCard)
        .padding(horizontal = 10.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = commentInput,
        onValueChange = { commentInput = it },
        placeholder = { Text("Trả lời khoảnh khắc này...", fontSize = 11.sp, color = TextSecondary) },
        modifier = Modifier.weight(1f),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.Transparent,
          unfocusedContainerColor = Color.Transparent,
          focusedBorderColor = Color.Transparent,
          unfocusedBorderColor = Color.Transparent,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        textStyle = MaterialTheme.typography.bodySmall
      )

      IconButton(
        onClick = {
          if (commentInput.isNotBlank()) {
            Toast.makeText(context, "Đã gửi tin nhắn đến ${moment.senderName}!", Toast.LENGTH_SHORT).show()
            commentInput = ""
            onOpenChat()
          }
        },
        modifier = Modifier.size(32.dp)
      ) {
        Icon(Icons.Default.Send, contentDescription = "Send", tint = LookGold, modifier = Modifier.size(16.dp))
      }
    }
  }
}
