package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AiCompanionScreen
import com.example.ui.screens.CaptureScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.Web3VaultScreen
import com.example.ui.theme.LookBorder
import com.example.ui.theme.LookDarkBg
import com.example.ui.theme.LookDarkSurface
import com.example.ui.theme.LookGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        LookAppMain()
      }
    }
  }
}

@Composable
fun LookAppMain() {
  var selectedTab by remember { mutableIntStateOf(0) }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(LookDarkBg),
    bottomBar = {
      NavigationBar(
        containerColor = LookDarkSurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp
      ) {
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          icon = {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = "Chụp ảnh"
            )
          },
          label = {
            Text(
              "Chụp ảnh",
              fontSize = 11.sp,
              fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = LookGold,
            indicatorColor = LookGold,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          )
        )

        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          icon = {
            Icon(
              imageVector = Icons.Default.Widgets,
              contentDescription = "Widget Feed"
            )
          },
          label = {
            Text(
              "Khoảnh khắc",
              fontSize = 11.sp,
              fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = LookGold,
            indicatorColor = LookGold,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          )
        )

        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          icon = {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Look AI"
            )
          },
          label = {
            Text(
              "Look AI",
              fontSize = 11.sp,
              fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = LookGold,
            indicatorColor = LookGold,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          )
        )

        NavigationBarItem(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          icon = {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Web3 Vault"
            )
          },
          label = {
            Text(
              "Web3 Vault",
              fontSize = 11.sp,
              fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = LookGold,
            indicatorColor = LookGold,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          )
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(LookDarkBg)
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        0 -> CaptureScreen(onMomentSent = { selectedTab = 1 })
        1 -> FeedScreen(
          onNavigateToCapture = { selectedTab = 0 },
          onNavigateToChat = { selectedTab = 2 }
        )
        2 -> AiCompanionScreen()
        3 -> Web3VaultScreen()
      }
    }
  }
}
