package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.LookMoment
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkCard
import com.example.ui.theme.LookGold
import com.example.ui.theme.LookNeonCyan
import com.example.ui.theme.LookPurple

@Composable
fun LookWidgetPreview(
  moment: LookMoment,
  modifier: Modifier = Modifier,
  isHomeScreenSimulator: Boolean = false
) {
  Column(modifier = modifier) {
    if (isHomeScreenSimulator) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(LookGold)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "LOOK LIVE WIDGET (2x2)",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = LookGold
            )
          )
        }
        Text(
          text = "Đang đồng bộ",
          style = MaterialTheme.typography.labelSmall.copy(color = LookNeonCyan)
        )
      }
    }

    // Widget Body
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(32.dp))
        .background(LookDarkCard)
        .border(
          width = 2.dp,
          brush = Brush.linearGradient(
            colors = listOf(
              LookGold.copy(alpha = 0.8f),
              LookPurple.copy(alpha = 0.5f),
              LookNeonCyan.copy(alpha = 0.4f)
            )
          ),
          shape = RoundedCornerShape(32.dp)
        )
    ) {
      // Background Moment Image
      AsyncImage(
        model = moment.imageUrl,
        contentDescription = "Widget Moment",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Top Tag: Sender badge
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(14.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(Color.Black.copy(alpha = 0.75f))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        AsyncImage(
          model = moment.senderAvatar,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = moment.senderName,
          color = Color.White,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "• ${moment.timeAgo}",
          color = Color.White.copy(alpha = 0.7f),
          fontSize = 11.sp
        )
      }

      // Top-Right SBT Badge if minted
      if (moment.blockchainMetadata != null) {
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(14.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LookPurple.copy(alpha = 0.9f))
            .padding(horizontal = 8.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "Soulbound NFT",
            tint = Color.White,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "SBT #${moment.blockchainMetadata.tokenId.takeLast(4)}",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }

      // Bottom Gradient Overlay & AI Caption
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.4f),
                Color.Black.copy(alpha = 0.92f)
              )
            )
          )
          .padding(16.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "AI Caption",
              tint = LookGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "LOOK AI CAPTION",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = LookGold
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = moment.aiMetadata.caption,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}
