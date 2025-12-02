package com.example.lumena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.lumena.ui.theme.LumenaTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lumena.data.onboarding.OnboardingPrefs
import com.example.lumena.data.onboarding.OnboardingRepositoryImpl
import com.example.lumena.ui.screens.CheckInScreen
import com.example.lumena.ui.screens.HomeScreen
import com.example.lumena.ui.screens.OnboardingScreen
import com.example.lumena.ui.screens.ToolsScreen
import com.example.lumena.ui.screens.TrendsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LumenaTheme {
                LumenaApp()
            }
        }
    }
}

@Composable
fun LumenaApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { OnboardingPrefs(context) }
    val repo = remember { OnboardingRepositoryImpl(prefs) }

    val completed by repo.onboardingCompleted().collectAsState(initial = false)
    val startDestination = if (completed) "home" else "onboarding"




    NavHost(
        navController = navController,
        startDestination = startDestination

    ) {
        composable("onboarding") {
            OnboardingScreen(
                onContinue = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onCheckInClick = { navController.navigate("checkin") },
                onToolsClick = { navController.navigate("tools") },
                onTrendsClick = { navController.navigate("trends") }
            )
        }
        composable("checkin") {
            CheckInScreen(onBack = { navController.popBackStack() })
        }
        composable("tools") {
            ToolsScreen(onBack = { navController.popBackStack() })
        }
        composable("trends") {
            TrendsScreen(onBack = { navController.popBackStack() })
        }
    }
}