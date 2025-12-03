package com.example.lumena

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.lumena.ui.theme.LumenaTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.lumena.data.mood.MoodDatabase
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.data.mood.RoomMoodRepository
import com.example.lumena.data.onboarding.OnboardingPrefs
import com.example.lumena.data.onboarding.OnboardingRepositoryImpl
import com.example.lumena.ui.screens.AboutSafetyScreen
import com.example.lumena.ui.screens.CheckInScreen
import com.example.lumena.ui.screens.HomeScreen
import com.example.lumena.ui.screens.OnboardingScreen
import com.example.lumena.ui.screens.ToolsScreen
import com.example.lumena.ui.screens.TrendsScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LumenaTheme {
                LumenaApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType")
@Composable
fun LumenaApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Onboarding prefs + repo (existing)
    val prefs = remember { OnboardingPrefs(context) }
    val onboardingRepo = remember { OnboardingRepositoryImpl(prefs) }
    val completed by onboardingRepo.onboardingCompleted().collectAsState(initial = false)
    val startDestination = if (completed) "home" else "onboarding"

    // Room DB + Mood repo (create once)
    val db = remember {
        Room.databaseBuilder(
            context,
            MoodDatabase::class.java,
            "lumena-db"
        ).build()
    }
    // Use your concrete Room repository implementation
    val moodRepo = remember { RoomMoodRepository(db.moodDao()) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("onboarding") {
            OnboardingScreen(
                repo = onboardingRepo,
                onContinue = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }


        composable("home") {
            HomeScreen(
                onboardingRepo = onboardingRepo,
                moodRepo = moodRepo,
                onCheckInClick = { navController.navigate("checkin") },
                onToolsClick = { navController.navigate("tools") },
                onTrendsClick = { navController.navigate("trends") },
                onAboutClick = { navController.navigate("about") }
            )


    }

        composable("checkin") {
            // Pass moodRepo to CheckInScreen
            CheckInScreen(
                moodRepo = moodRepo,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tools") {
            ToolsScreen(onBack = { navController.popBackStack() })
        }

        composable("trends") {
            TrendsScreen(
                moodRepo = moodRepo,
                onBack = { navController.popBackStack() }
            )
        }

        composable("about") {
            AboutSafetyScreen(onBack = { navController.popBackStack() })
        }


    }

}
