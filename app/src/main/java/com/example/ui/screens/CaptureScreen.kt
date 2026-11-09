package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LookRepository
import com.example.model.AudienceVisibility
import com.example.model.MediaType
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkCard
import com.example.ui.theme.LookDarkSurface
import com.example.ui.theme.LookGold
import com.example.ui.theme.LookNeonCyan
import com.example.ui.theme.LookPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

data class FriendAccount(
  val username: String,
  val fullName: String,
  val avatarUrl: String
)

private val sampleFriends = listOf(
  FriendAccount("minhquan", "Minh Quân", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80"),
  FriendAccount("hamy", "Hà My", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80"),
  FriendAccount("baotram", "Bảo Trâm", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80"),
  FriendAccount("tuananh", "Tuấn Anh", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80")
)

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
  val streakCount by LookRepository.currentStreak.collectAsState()

  // 1. Trạng thái ĐÃ CHỤP ẢNH hay CHƯA
  var hasCapturedPhoto by remember { mutableStateOf(false) }

  var isFlashOn by remember { mutableStateOf(false) }
  var isFrontCamera by remember { mutableStateOf(false) }

  // Photo / Video
  var currentPhotoPath by remember { mutableStateOf(sampleCameraCaptures[0]) }
  var isFromDeviceCamera by remember { mutableStateOf(false) }
  var mediaType by remember { mutableStateOf(MediaType.PHOTO) }
  var isRecordingVideo by remember { mutableStateOf(false) }
  var recordProgress by remember { mutableFloatStateOf(0f) }
  var recordingJob by remember { mutableStateOf<Job?>(null) }

  // 2. Chế độ xem Dropdown (Audience Selector)
  var selectedAudience by remember { mutableStateOf(AudienceVisibility.CLOSE_FRIENDS) }
  var isAudienceMenuExpanded by remember { mutableStateOf(false) }

  // 3. Caption & Mention @ Gợi ý bạn bè
  var selectedMood by remember { mutableStateOf("Hài hước") }
  var captionSuggestions by remember { mutableStateOf(aiCaptionPresets["Hài hước"] ?: emptyList()) }
  var captionText by remember { mutableStateOf("") }
  var isGeneratingAi by remember { mutableStateOf(false) }
  var isSending by remember { mutableStateOf(false) }
  var isMintSbt by remember { mutableStateOf(true) }

  // 4. Tạo nhãn và Gán nhãn cho ảnh
  val photoTags = remember { mutableStateListOf<String>("#LookMoment", "#HồTây", "#Weekend") }
  var customTagInput by remember { mutableStateOf("") }
  val suggestedPresetTags = listOf("#BạnThân", "#CàPhê", "#HọcBài", "#Deadline", "#Foodie", "#Chill", "#DuLịch", "#CheckIn")

  val previewTagsScrollState = rememberScrollState()
  val assignedTagsScrollState = rememberScrollState()
  val suggestedTagsScrollState = rememberScrollState()

  fun addTag(rawTag: String) {
    val clean = rawTag.trim().replace(" ", "")
    if (clean.isNotEmpty()) {
      val formatted = if (clean.startsWith("#")) clean else "#$clean"
      if (!photoTags.contains(formatted)) {
        photoTags.add(formatted)
      }
    }
  }

  // Kiểm tra xem người dùng có đang gõ ký tự '@' không
  val mentionQuery = remember(captionText) {
    val lastAt = captionText.lastIndexOf('@')
    if (lastAt >= 0) {
      val afterAt = captionText.substring(lastAt + 1)
      if (!afterAt.contains(' ')) afterAt.lowercase() else null
    } else null
  }

  val matchingFriends = remember(mentionQuery) {
    if (mentionQuery != null) {
      sampleFriends.filter {
        it.username.lowercase().contains(mentionQuery) ||
        it.fullName.lowercase().contains(mentionQuery)
      }
    } else emptyList()
  }

  fun triggerAiCaption(mood: String) {
    selectedMood = mood
    isGeneratingAi = true
    coroutineScope.launch {
      delay(500)
      val options = aiCaptionPresets[mood] ?: aiCaptionPresets["Hài hước"]!!
      captionSuggestions = options
      captionText = options.first()
      isGeneratingAi = false
    }
  }

  // Camera Launcher
  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview(),
    onResult = { bitmap ->
      if (bitmap != null) {
        val savedPath = saveBitmapToCache(context, bitmap)
        currentPhotoPath = savedPath
        isFromDeviceCamera = true
        mediaType = MediaType.PHOTO
        hasCapturedPhoto = true // Đã chụp -> Chuyển sang hiển thị caption
        triggerAiCaption(selectedMood)
        Toast.makeText(context, "📸 Đã chụp ảnh thành công!", Toast.LENGTH_SHORT).show()
      }
    }
  )

  // Photo Picker Launcher
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = { uri ->
      if (uri != null) {
        try {
          val savedPath = copyUriToCache(context, uri)
          currentPhotoPath = savedPath
          isFromDeviceCamera = true
          mediaType = MediaType.PHOTO
          hasCapturedPhoto = true // Đã chọn -> Chuyển sang hiển thị caption
          triggerAiCaption(selectedMood)
          Toast.makeText(context, "🖼️ Đã chọn ảnh từ thư viện!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
          Toast.makeText(context, "Lỗi tải ảnh: ${e.message}", Toast.LENGTH_SHORT).show()
        }
      }
    }
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // TOP BAR: Flash, Streak 🔥, Dropdown Chế độ xem với Mũi tên nhỏ
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
          .size(40.dp)
      ) {
        Icon(
          imageVector = Icons.Default.FlashOn,
          contentDescription = "Flash",
          tint = if (isFlashOn) Color.Black else TextPrimary,
          modifier = Modifier.size(20.dp)
        )
      }

      // Dropdown Chế độ xem với Mũi tên nhỏ (Dropdown Arrow)
      Box {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(LookDarkSurface)
            .border(1.dp, LookGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .clickable { isAudienceMenuExpanded = true }
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = when (selectedAudience) {
              AudienceVisibility.CLOSE_FRIENDS -> Icons.Default.Stars
              AudienceVisibility.PUBLIC -> Icons.Default.Public
              AudienceVisibility.SPECIFIC -> Icons.Default.People
            },
            contentDescription = null,
            tint = LookGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = when (selectedAudience) {
              AudienceVisibility.CLOSE_FRIENDS -> "Bạn thân (Widget)"
              AudienceVisibility.PUBLIC -> "Công khai"
              AudienceVisibility.SPECIFIC -> "Chỉ định"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.width(2.dp))
          // Mũi tên nhỏ Dropdown
          Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown Menu",
            tint = LookGold,
            modifier = Modifier.size(20.dp)
          )
        }

        DropdownMenu(
          expanded = isAudienceMenuExpanded,
          onDismissRequest = { isAudienceMenuExpanded = false },
          modifier = Modifier.background(LookDarkSurface)
        ) {
          DropdownMenuItem(
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Stars, contentDescription = null, tint = LookGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Bạn thân (Live Widget)", fontWeight = FontWeight.Bold, color = TextPrimary)
                  Text("Chỉ hiển thị ra màn hình chính của hội bạn thân", fontSize = 10.sp, color = TextSecondary)
                }
              }
            },
            onClick = {
              selectedAudience = AudienceVisibility.CLOSE_FRIENDS
              isAudienceMenuExpanded = false
            }
          )
          DropdownMenuItem(
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Public, contentDescription = null, tint = LookNeonCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Công khai", fontWeight = FontWeight.Bold, color = TextPrimary)
                  Text("Tất cả bạn bè trên Look đều xem được", fontSize = 10.sp, color = TextSecondary)
                }
              }
            },
            onClick = {
              selectedAudience = AudienceVisibility.PUBLIC
              isAudienceMenuExpanded = false
            }
          )
          DropdownMenuItem(
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.People, contentDescription = null, tint = LookPurple, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Chỉ định người nhận", fontWeight = FontWeight.Bold, color = TextPrimary)
                  Text("Gửi riêng cho các bạn bè được gắn thẻ", fontSize = 10.sp, color = TextSecondary)
                }
              }
            },
            onClick = {
              selectedAudience = AudienceVisibility.SPECIFIC
              isAudienceMenuExpanded = false
            }
          )
        }
      }

      // Streak Flame Indicator
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFF2E1700))
          .border(1.dp, LookGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.LocalFireDepartment,
          contentDescription = "Streak",
          tint = Color(0xFFFF6D00),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = "$streakCount",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            color = LookGold
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // KHUNG HÌNH CHỤP ẢNH / PREVIEW (SQUIRCLE 1:1)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(36.dp))
        .background(LookDarkCard)
        .border(
          width = 3.dp,
          brush = Brush.sweepGradient(
            if (isRecordingVideo)
              listOf(Color.Red, Color(0xFFFF5252), Color.Red)
            else
              listOf(LookGold, LookPurple, LookNeonCyan, LookGold)
          ),
          shape = RoundedCornerShape(36.dp)
        )
    ) {
      AsyncImage(
        model = currentPhotoPath,
        contentDescription = "Camera Viewfinder",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Huy hiệu trạng thái trên khung ảnh
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(14.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.75f))
          .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (mediaType == MediaType.VIDEO) Icons.Default.Videocam else Icons.Default.CameraAlt,
          contentDescription = null,
          tint = if (isRecordingVideo) Color.Red else LookGold,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (!hasCapturedPhoto) "Sẵn sàng chụp" else (if (mediaType == MediaType.VIDEO) "Video 10s" else "Ảnh đã chụp"),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      // Nút Chụp lại (Retake) khi đã chụp ảnh
      if (hasCapturedPhoto) {
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(14.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable {
              hasCapturedPhoto = false
              captionText = ""
            }
            .padding(horizontal = 8.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Refresh, contentDescription = "Chụp lại", tint = LookGold, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Chụp lại", fontSize = 11.sp, color = LookGold, fontWeight = FontWeight.Bold)
        }
      }

      // Hiển thị các nhãn đã gán trên góc dưới ảnh
      if (hasCapturedPhoto && photoTags.isNotEmpty()) {
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
            .horizontalScroll(previewTagsScrollState),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          photoTags.forEach { tag ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.8f))
                .border(0.5.dp, LookGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
              Text(text = tag, color = LookGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Thanh đổi ảnh mẫu nhanh ở dưới đáy khung ngắm
      if (!hasCapturedPhoto) {
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
          Text(text = "Mẫu:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
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
                  hasCapturedPhoto = true // Chọn ảnh -> Chuyển sang hiển thị caption
                  triggerAiCaption(selectedMood)
                }
            ) {
              AsyncImage(model = url, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // 4. NÚT CAMERA Ở CHÍNH GIỮA KHUNG ẢNH (Bố cục chuẩn Locket: Album bên trái - Nút Camera ở GIỮA - Đổi Camera bên phải)
    if (!hasCapturedPhoto) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Nút Album bên trái
        IconButton(
          onClick = {
            photoPickerLauncher.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          },
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(LookDarkSurface)
            .border(1.dp, LookBorder, CircleShape)
        ) {
          Icon(
            imageVector = Icons.Default.PhotoLibrary,
            contentDescription = "Chọn từ Album",
            tint = LookNeonCyan,
            modifier = Modifier.size(24.dp)
          )
        }

        // NÚT CAMERA Ở CHÍNH GIỮA (Lớn 76dp, Tap = Chụp, Long-press = Quay 10s)
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(if (isRecordingVideo) Color.Red else LookGold)
            .border(4.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            .pointerInput(Unit) {
              detectTapGestures(
                onTap = {
                  mediaType = MediaType.PHOTO
                  cameraLauncher.launch(null)
                },
                onLongPress = {
                  mediaType = MediaType.VIDEO
                  isRecordingVideo = true
                  recordProgress = 0f
                  recordingJob = coroutineScope.launch {
                    for (i in 1..100) {
                      delay(100)
                      recordProgress = i / 100f
                    }
                    isRecordingVideo = false
                    hasCapturedPhoto = true
                    triggerAiCaption(selectedMood)
                    Toast.makeText(context, "Đã quay xong video 10s!", Toast.LENGTH_SHORT).show()
                  }
                }
              )
            },
          contentAlignment = Alignment.Center
        ) {
          if (isRecordingVideo) {
            CircularProgressIndicator(
              progress = { recordProgress },
              modifier = Modifier.size(74.dp),
              color = Color.White,
              strokeWidth = 4.dp
            )
          }
          Icon(
            imageVector = if (isRecordingVideo) Icons.Default.Videocam else Icons.Default.CameraAlt,
            contentDescription = "Nút Camera ở giữa",
            tint = Color.Black,
            modifier = Modifier.size(34.dp)
          )
        }

        // Nút Đảo Camera bên phải
        IconButton(
          onClick = {
            isFrontCamera = !isFrontCamera
            cameraLauncher.launch(null)
          },
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(LookDarkSurface)
            .border(1.dp, LookBorder, CircleShape)
        ) {
          Icon(
            imageVector = Icons.Default.Cameraswitch,
            contentDescription = "Đổi Camera",
            tint = LookGold,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Text(
        text = "Chạm nút ở giữa để Chụp • Giữ để Quay video 10s",
        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp),
        modifier = Modifier.padding(top = 4.dp)
      )
    }

    // 1. SAU KHI CHỤP ẢNH RỒI MỚI HIỂN THỊ PHẦN CAPTION & GỢI Ý BẠN BÈ @
    AnimatedVisibility(
      visible = hasCapturedPhoto,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // KHỐI LOOK AI CAPTION
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(LookDarkSurface)
            .border(1.dp, LookGold.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
            .padding(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = LookGold, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("LOOK AI CAPTION CHO ẢNH", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = LookGold))
            }

            if (isGeneratingAi) {
              CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = LookGold)
            } else {
              Text("Gemini Vision AI", fontSize = 11.sp, color = TextSecondary)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Lựa chọn Mood
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
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) LookGold.copy(alpha = 0.2f) else LookDarkCard)
                  .border(1.dp, if (isSelected) LookGold else LookBorder, RoundedCornerShape(12.dp))
                  .clickable { triggerAiCaption(mood) }
                  .padding(horizontal = 10.dp, vertical = 5.dp)
              ) {
                Text(
                  text = "✨ $mood",
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) LookGold else TextPrimary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 3-4 Câu gợi ý từ AI để người dùng bấm chọn nhanh
          captionSuggestions.take(3).forEach { suggestion ->
            val isChosen = captionText == suggestion
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isChosen) LookGold.copy(alpha = 0.15f) else LookDarkCard)
                .border(1.dp, if (isChosen) LookGold else LookBorder, RoundedCornerShape(10.dp))
                .clickable { captionText = suggestion }
                .padding(10.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (isChosen) "👉" else "💬", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = suggestion,
                  fontSize = 12.sp,
                  color = if (isChosen) LookGold else TextPrimary,
                  fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Ô NHẬP CAPTION (HỖ TRỢ GÕ @ ĐỂ TAG BẠN BÈ)
          OutlinedTextField(
            value = captionText,
            onValueChange = { captionText = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            placeholder = { Text("Viết caption hoặc gõ @ để gắn thẻ bạn bè...", fontSize = 12.sp, color = TextSecondary) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = LookDarkCard,
              unfocusedContainerColor = LookDarkCard,
              focusedBorderColor = LookGold,
              unfocusedBorderColor = LookBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            textStyle = MaterialTheme.typography.bodySmall
          )

          // 3. KHI GÕ @ THÌ HIỂN THỊ POPUP / DANH SÁCH ACC BẠN BÈ
          if (matchingFriends.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(LookDarkCard)
                .border(1.dp, LookGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(8.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 6.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Gợi ý gắn thẻ bạn bè:", fontSize = 11.sp, color = LookGold, fontWeight = FontWeight.Bold)
                Text("Chạm để chọn", fontSize = 10.sp, color = TextSecondary)
              }

              matchingFriends.forEach { friend ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                      // Thay thế phần @ đang gõ bằng @username kèm khoảng trắng
                      val lastAt = captionText.lastIndexOf('@')
                      if (lastAt >= 0) {
                        val beforeAt = captionText.substring(0, lastAt)
                        captionText = "$beforeAt@${friend.username} "
                      }
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  AsyncImage(
                    model = friend.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                      .size(28.dp)
                      .clip(CircleShape)
                      .border(1.dp, LookGold, CircleShape)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(text = "@${friend.username}", fontWeight = FontWeight.Bold, color = LookGold, fontSize = 12.sp)
                    Text(text = friend.fullName, color = TextSecondary, fontSize = 10.sp)
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // KHỐI TẠO & GÁN NHÃN CHO ẢNH (Tags & Labels)
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
              Icon(Icons.Default.Label, contentDescription = null, tint = LookNeonCyan, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("GÁN NHÃN CHO ẢNH (${photoTags.size})", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
            }
            Text("Thẻ phân loại", fontSize = 11.sp, color = LookNeonCyan)
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Danh sách các nhãn đang gán vào ảnh
          if (photoTags.isNotEmpty()) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(assignedTagsScrollState),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              photoTags.forEach { tag ->
                Row(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(LookGold.copy(alpha = 0.15f))
                    .border(1.dp, LookGold, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = tag, color = LookGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.width(6.dp))
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Xóa nhãn",
                    tint = LookGold,
                    modifier = Modifier
                      .size(14.dp)
                      .clickable { photoTags.remove(tag) }
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
          }

          // Ô nhập nhãn tự do
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = customTagInput,
              onValueChange = { customTagInput = it },
              placeholder = { Text("Tạo nhãn mới (vd: KỷNiệm, ĐiChơi...)", fontSize = 11.sp, color = TextSecondary) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LookDarkCard,
                unfocusedContainerColor = LookDarkCard,
                focusedBorderColor = LookNeonCyan,
                unfocusedBorderColor = LookBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              textStyle = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
              onClick = {
                if (customTagInput.isNotBlank()) {
                  addTag(customTagInput)
                  customTagInput = ""
                }
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = LookDarkCard, contentColor = LookNeonCyan),
              border = androidx.compose.foundation.BorderStroke(1.dp, LookNeonCyan),
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Gán nhãn", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Gợi ý nhãn nhanh từ hệ thống
          Text("Nhãn gợi ý nhanh:", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(suggestedTagsScrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            suggestedPresetTags.forEach { suggested ->
              val isAssigned = photoTags.contains(suggested)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isAssigned) LookDarkSurface else LookDarkCard)
                  .border(0.5.dp, if (isAssigned) LookBorder else LookNeonCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .clickable {
                  if (isAssigned) photoTags.remove(suggested) else photoTags.add(suggested)
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = if (isAssigned) "$suggested ✓" else "+ $suggested",
                fontSize = 10.sp,
                color = if (isAssigned) TextSecondary else LookNeonCyan,
                fontWeight = if (isAssigned) FontWeight.Normal else FontWeight.SemiBold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Toggle Web3 Soulbound Token
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(LookDarkSurface)
          .border(1.dp, if (isMintSbt) LookPurple.copy(alpha = 0.6f) else LookBorder, RoundedCornerShape(16.dp))
          .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = LookPurple, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text("Đúc Soulbound NFT Kỷ Niệm", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
            Text("Mã hóa IPFS & Polygon SBT vĩnh cửu", fontSize = 10.sp, color = TextSecondary)
          }
        }

        Switch(
          checked = isMintSbt,
          onCheckedChange = { isMintSbt = it },
          colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = LookPurple, uncheckedTrackColor = LookDarkCard)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // NÚT GỬI ĐẾN LIVE WIDGET BẠN BÈ
      Button(
        onClick = {
          isSending = true
          coroutineScope.launch {
            delay(800)
            LookRepository.addNewMoment(
              imageUrl = currentPhotoPath,
              caption = captionText.ifBlank { "Một khoảnh khắc tuyệt vời từ Look! ✨" },
              mood = selectedMood,
              tags = photoTags.toList(),
              mintSbt = isMintSbt,
              mediaType = mediaType,
              audience = selectedAudience
            )
            isSending = false
            Toast.makeText(context, "Đã gửi lên Live Widget bạn bè thành công! 🚀", Toast.LENGTH_LONG).show()
            onMomentSent()
          }
        },
          enabled = !isSending,
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.buttonColors(containerColor = LookGold, contentColor = Color.Black)
        ) {
          if (isSending) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 3.dp, color = Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Đang đồng bộ Live Widget...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          } else {
            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("GỬI ĐẾN LIVE WIDGET BẠN BÈ", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
