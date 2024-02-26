package com.lmar.budgetcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lmar.budgetcalc.ui.screens.BudgetCalcNavigationGraph
import com.lmar.budgetcalc.ui.screens.Routes
import com.lmar.budgetcalc.ui.screens.UserInputScreen
import com.lmar.budgetcalc.ui.screens.WelcomeScreen
import com.lmar.budgetcalc.ui.theme.BudgetCalcTheme

class MainActivity : ComponentActivity() {
    //Tutorial
    //https://www.youtube.com/watch?v=dEEyZkZekvI&t=881s

    //Emoji
    //https://emojipedia.org/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BudgetCalcTheme {
                BudgetCalcApp()
            }
        }
    }
}

@Composable
fun BudgetCalcApp() {
    BudgetCalcNavigationGraph()

}
