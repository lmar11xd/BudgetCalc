package com.lmar.budgetcalc.feature.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lmar.budgetcalc.feature.presentation.budget_list.BudgetListScreen
import com.lmar.budgetcalc.feature.presentation.budget_list.BudgetListViewModel
import com.lmar.budgetcalc.feature.presentation.budget_new_update.BudgetNewUpdateScreen
import com.lmar.budgetcalc.feature.presentation.util.Screen
import com.lmar.budgetcalc.ui.theme.BudgetCalcTheme
import com.lmar.budgetcalc.ui.screens.BudgetCalcNavigationGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    val listViewModel: BudgetListViewModel = hiltViewModel()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.BudgetListScreen.route,
                    ) {
                        composable(route = Screen.BudgetListScreen.route){
                            BudgetListScreen(
                                navController = navController,
                                viewModel = listViewModel
                            )
                        }

                        composable(
                            route = Screen.BudgetNewUpdateScreen.route + "?budgetId={budgetId}",
                            arguments = listOf(
                                navArgument(
                                    name = "budgetId"
                                ){
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ){
                            BudgetNewUpdateScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetCalcApp() {
    BudgetCalcNavigationGraph()

}
