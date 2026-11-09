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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LookRepository
import com.example.model.LookMoment
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
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val moments by LookRepository.moments.collectAsState()
  val activeWidgetMoment by LookRepository.activeWidgetMoment.collectAsState()

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Live Widget Simulation Header
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
          Toast.makeText(context, "Đã đúc thành công Soulbound Token vĩnh cửu trên Polygon!", Toast.LENGTH_LONG).show()
        }
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
  onMintSbt: () -> Unit
) {
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
    // Top Sender info
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
          Text(
            text = moment.senderName,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
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
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
          )
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

    // AI Caption & Mood
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = LookGold,
        modifier = Modifier.size(14.dp)
      )
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

    Spacer(modifier = Modifier.height(8.dp))

    // Bottom Action Bar: Like & Web3 Mint Button
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
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
          text = "${moment.reactionsCount} thả tim",
          color = TextSecondary,
          fontSize = 12.sp
        )
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
          Text("Đúc SBT Kỷ Niệm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      } else {
        Text(
          text = "Đã lưu IPFS & Polygon",
          color = LookNeonCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}
