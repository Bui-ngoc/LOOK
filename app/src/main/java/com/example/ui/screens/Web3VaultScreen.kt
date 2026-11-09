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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LookRepository
import com.example.model.LookMoment
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkCard
import com.example.ui.theme.LookDarkSurface
import com.example.ui.theme.LookEmerald
import com.example.ui.theme.LookGold
import com.example.ui.theme.LookNeonCyan
import com.example.ui.theme.LookPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun Web3VaultScreen(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val walletAddress by LookRepository.walletAddress.collectAsState()
  val moments by LookRepository.moments.collectAsState()

  val mintedMoments = moments.filter { it.blockchainMetadata != null }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Wallet Status Card
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(LookDarkSurface)
          .border(1.dp, LookPurple.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
          .padding(18.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LookPurple.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = LookPurple,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Look Web3 Vault",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(LookEmerald)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Polygon PoS Network",
                  color = LookEmerald,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(LookPurple.copy(alpha = 0.2f))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(text = "4.82 POL", color = LookPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Wallet Address Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LookDarkCard)
            .border(1.dp, LookBorder, RoundedCornerShape(12.dp))
            .clickable {
              clipboardManager.setText(AnnotatedString(walletAddress))
              Toast.makeText(context, "Đã sao chép địa chỉ ví!", Toast.LENGTH_SHORT).show()
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = walletAddress,
            color = TextSecondary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
          )
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            tint = LookGold,
            modifier = Modifier.size(16.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // IPFS Security badge
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CloudDone,
            contentDescription = null,
            tint = LookNeonCyan,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Lưu trữ phi tập trung IPFS: Không sợ lộ lọt dữ liệu máy chủ trung tâm",
            color = LookNeonCyan,
            fontSize = 11.sp
          )
        }
      }
    }

    // Title for Minted SBTs
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Huy hiệu Tình bạn Vĩnh cửu (${mintedMoments.size} SBT)",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        )
        Text(
          text = "Soulbound (ERC-721)",
          color = LookPurple,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // List of Minted Soulbound Moments
    items(mintedMoments, key = { it.id }) { moment ->
      val meta = moment.blockchainMetadata!!
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(LookDarkSurface)
          .border(1.dp, LookBorder, RoundedCornerShape(20.dp))
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = LookGold,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Soulbound Token ${meta.tokenId}",
              fontWeight = FontWeight.Bold,
              color = LookGold,
              fontSize = 14.sp
            )
          }
          Text(
            text = meta.mintedAt,
            color = TextSecondary,
            fontSize = 11.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Thumbnail
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(LookDarkCard)
          ) {
            AsyncImage(
              model = moment.imageUrl,
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          // Metadata Info
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = moment.aiMetadata.caption,
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
              ),
              maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Người gửi: ${moment.senderName}",
              color = TextSecondary,
              fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "IPFS CID: ${meta.ipfsCid.take(16)}...",
              color = LookNeonCyan,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Transaction hash block
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LookDarkCard)
            .padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Tx: ${meta.txHash.take(22)}...",
            color = TextSecondary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
          Row(
            modifier = Modifier.clickable {
              clipboardManager.setText(AnnotatedString(meta.txHash))
              Toast.makeText(context, "Đã chép mã giao dịch Polygon!", Toast.LENGTH_SHORT).show()
            },
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("PolygonScan", color = LookPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(2.dp))
            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = LookPurple, modifier = Modifier.size(12.dp))
          }
        }
      }
    }
  }
}
