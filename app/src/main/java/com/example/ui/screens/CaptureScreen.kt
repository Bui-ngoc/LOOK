package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkCard
import com.example.ui.theme.LookDarkSurface
import com.example.ui.theme.LookEmerald
import com.example.ui.theme.LookGold
import com.example.ui.theme.LookNeonCyan
import com.example.ui.theme.LookPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

private val sampleCameraCaptures = listOf(
  "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=800&auto=format&fit=crop&q=80",
  "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=800&auto=format&fit=crop&q=80",
  "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800&auto=format&fit=crop&q=80",
  "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=800&auto=format&fit=crop&q=80"
)

private val aiCaptionPresets = mapOf(
  "Hài hước" to listOf(
    "Nụ cười 10 phân vẹn 10 nhưng bài tập chưa làm xong cái nào 😂",
    "Có những khoảnh khắc chỉ cần nhìn mặt nhau là đã buồn cười rồi!",
    "Bên ngoài thảnh thơi, bên trong gánh còng lưng deadline 🔥",
    "Ảnh chụp chất lượng cao, nhan sắc chất lượng đỉnh chóp 😎"
  ),
  "Thơ mộng" to listOf(
    "Mặt trời xuống núi nghỉ ngơi, gửi vào widget nụ cười của nhau ✨",
    "Tháng năm rực rỡ nhất là khi có các cậu cùng đồng hành 🌸",
    "Gió thổi mang hương chiều hạ, gửi chút bình yên sang màn hình bạn.",
    "Khoảnh khắc nhỏ bé nhưng đong đầy yêu thương 💛"
  ),
  "Hoài niệm" to listOf(
    "Sau này nhìn lại, chúng ta sẽ mỉm cười vì đã sống hết mình cho tuổi trẻ.",
    "Khoảnh khắc này sẽ được lưu giữ mãi mãi trong chiếc widget nhỏ.",
    "Cảm ơn vì đã luôn là tri kỷ trong từng khung hình kỷ niệm.",
    "Bảo vật thời gian, gửi vào hôm nay để nhớ về mai sau 🕰️"
  )
)

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): String {
  val file = File(context.cacheDir, "look_captured_${System.currentTimeMillis()}.jpg")
  FileOutputStream(file).use { out ->
    bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
  }
  return file.absolutePath
}

private fun copyUriToCache(context: Context, uri: Uri): String {
  val file = File(context.cacheDir, "look_picked_${System.currentTimeMillis()}.jpg")
  context.contentResolver.openInputStream(uri)?.use { input ->
    FileOutputStream(file).use { output ->
      input.copyTo(output)
    }
  }
  return file.absolutePath
}

@Composable
fun CaptureScreen(
  onMomentSent: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var isFlashOn by remember { mutableStateOf(false) }
  var isFrontCamera by remember { mutableStateOf(false) }

  // Current photo source: can be custom file path, picked uri, or preset URL
  var currentPhotoPath by remember { mutableStateOf(sampleCameraCaptures[0]) }
  var isFromDeviceCamera by remember { mutableStateOf(false) }

  var selectedMood by remember { mutableStateOf("Hài hước") }
  var captionText by remember { mutableStateOf("Nụ cười 10 phân vẹn 10 nhưng bài tập chưa làm xong cái nào 😂") }
  var isMintSbt by remember { mutableStateOf(true) }
  var isGeneratingAi by remember { mutableStateOf(false) }
  var isSending by remember { mutableStateOf(false) }

  fun triggerAiCaption(mood: String) {
    selectedMood = mood
    isGeneratingAi = true
    coroutineScope.launch {
      delay(600)
      val options = aiCaptionPresets[mood] ?: aiCaptionPresets["Hài hước"]!!
      captionText = options.random()
      isGeneratingAi = false
    }
  }

  // 1. Hardware / System Camera Launcher (Opens camera app / emulator virtual camera)
  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview(),
    onResult = { bitmap ->
      if (bitmap != null) {
        val savedPath = saveBitmapToCache(context, bitmap)
        currentPhotoPath = savedPath
        isFromDeviceCamera = true
        Toast.makeText(context, "📸 Đã chụp ảnh thành công từ Camera máy!", Toast.LENGTH_SHORT).show()
        triggerAiCaption(selectedMood)
      }
    }
  )

  // 2. Android Photo Picker Launcher (Pick any photo from device storage)
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = { uri ->
      if (uri != null) {
        try {
          val savedPath = copyUriToCache(context, uri)
          currentPhotoPath = savedPath
          isFromDeviceCamera = true
          Toast.makeText(context, "🖼️ Đã chọn ảnh từ thư viện thiết bị!", Toast.LENGTH_SHORT).show()
          triggerAiCaption(selectedMood)
        } catch (e: Exception) {
          Toast.makeText(context, "Không thể tải ảnh: ${e.message}", Toast.LENGTH_SHORT).show()
        }
      }
    }
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Controls Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = { isFlashOn = !isFlashOn },
        modifier = Modifier
          .clip(CircleShape)
          .background(if (isFlashOn) LookGold else LookDarkCard)
      ) {
        Icon(
          imageVector = Icons.Default.FlashOn,
          contentDescription = "Flash",
          tint = if (isFlashOn) Color.Black else TextPrimary
        )
      }

      Text(
        text = "LOOK LIVE CAMERA",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 1.sp,
          color = LookGold
        )
      )

      IconButton(
        onClick = {
          isFrontCamera = !isFrontCamera
          // Launch camera or toggle view
          cameraLauncher.launch(null)
        },
        modifier = Modifier
          .clip(CircleShape)
          .background(LookDarkCard)
      ) {
        Icon(
          imageVector = Icons.Default.Cameraswitch,
          contentDescription = "Switch Camera",
          tint = TextPrimary
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Camera Viewfinder (Locket style squircle 1:1)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(36.dp))
        .background(LookDarkCard)
        .border(
          width = 3.dp,
          brush = Brush.sweepGradient(listOf(LookGold, LookPurple, LookNeonCyan, LookGold)),
          shape = RoundedCornerShape(36.dp)
        )
    ) {
      AsyncImage(
        model = currentPhotoPath,
        contentDescription = "Camera Viewfinder",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Live tag / Camera Source tag
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(14.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.7f))
          .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isFromDeviceCamera) LookEmerald else LookGold)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isFromDeviceCamera) "Ảnh trực tiếp từ máy" else "Camera Sẵn sàng",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
      }

      // Quick photo preset thumbnails inside viewfinder
      Row(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 12.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(Color.Black.copy(alpha = 0.65f))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Mẫu:",
          fontSize = 11.sp,
          color = Color.White.copy(alpha = 0.7f),
          fontWeight = FontWeight.Bold
        )
        sampleCameraCaptures.forEachIndexed { index, url ->
          val isSelected = currentPhotoPath == url
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) LookGold else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
              )
              .clickable {
                currentPhotoPath = url
                isFromDeviceCamera = false
                triggerAiCaption(selectedMood)
              }
          ) {
            AsyncImage(
              model = url,
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Direct Camera & Photo Library Buttons Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = { cameraLauncher.launch(null) },
        modifier = Modifier
          .weight(1f)
          .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = LookDarkSurface,
          contentColor = LookGold
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, LookGold)
      ) {
        Icon(
          imageVector = Icons.Default.CameraAlt,
          contentDescription = "Chụp Camera",
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Mở Camera máy",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
      }

      Button(
        onClick = {
          photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
          )
        },
        modifier = Modifier
          .weight(1f)
          .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = LookDarkSurface,
          contentColor = LookNeonCyan
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, LookNeonCyan)
      ) {
        Icon(
          imageVector = Icons.Default.PhotoLibrary,
          contentDescription = "Chọn từ Album",
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Thư viện ảnh",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Look AI Smart Caption Box
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
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = LookGold,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "LOOK AI COMPANION",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = LookGold
            )
          )
        }

        if (isGeneratingAi) {
          CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp,
            color = LookGold
          )
        } else {
          Text(
            text = "Gemini Vision AI",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Mood Selection Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("Hài hước", "Thơ mộng", "Hoài niệm").forEach { mood ->
          val isSelected = selectedMood == mood
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) LookGold.copy(alpha = 0.2f) else LookDarkCard)
              .border(
                1.dp,
                if (isSelected) LookGold else LookBorder,
                RoundedCornerShape(14.dp)
              )
              .clickable { triggerAiCaption(mood) }
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = "✨ $mood",
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) LookGold else TextPrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Caption Editable Field
      OutlinedTextField(
        value = captionText,
        onValueChange = { captionText = it },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 3,
        shape = RoundedCornerShape(14.dp),
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
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Web3 Vault & SBT Toggle Box
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp))
        .background(LookDarkSurface)
        .border(1.dp, if (isMintSbt) LookPurple.copy(alpha = 0.6f) else LookBorder, RoundedCornerShape(18.dp))
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(LookPurple.copy(alpha = 0.25f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = LookPurple,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Lưu trữ Web3 & Đúc Soulbound NFT",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Text(
            text = "Lưu IPFS phi tập trung & Huy hiệu Polygon vĩnh cửu",
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 11.sp,
              color = TextSecondary
            )
          )
        }
      }

      Switch(
        checked = isMintSbt,
        onCheckedChange = { isMintSbt = it },
        colors = SwitchDefaults.colors(
          checkedThumbColor = Color.White,
          checkedTrackColor = LookPurple,
          uncheckedTrackColor = LookDarkCard
        )
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Main Big Shutter & Send to Widget Button
    Button(
      onClick = {
        isSending = true
        coroutineScope.launch {
          delay(800)
          LookRepository.addNewMoment(
            imageUrl = currentPhotoPath,
            caption = captionText,
            mood = selectedMood,
            tags = listOf("#LookMoment", "#LiveWidget", "#$selectedMood"),
            mintSbt = isMintSbt
          )
          isSending = false
          Toast.makeText(
            context,
            "Đã đồng bộ ảnh mới ra Live Widget bạn bè thành công! 🚀",
            Toast.LENGTH_LONG
          ).show()
          onMomentSent()
        }
      },
      enabled = !isSending,
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp),
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = LookGold,
        contentColor = Color.Black
      )
    ) {
      if (isSending) {
        CircularProgressIndicator(
          modifier = Modifier.size(24.dp),
          strokeWidth = 3.dp,
          color = Color.Black
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = if (isMintSbt) "Đang mã hóa IPFS & gửi..." else "Đang đồng bộ Widget...",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      } else {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "GỬI ĐẾN LIVE WIDGET BẠN BÈ",
          fontWeight = FontWeight.ExtraBold,
          fontSize = 15.sp,
          letterSpacing = 0.5.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}
