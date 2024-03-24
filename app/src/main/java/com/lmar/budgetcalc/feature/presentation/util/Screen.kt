package com.lmar.budgetcalc.feature.presentation.util

sealed class Screen(val route: String) {
    object BudgetListScreen: Screen("budgetList_screen")
    object BudgetNewUpdateScreen: Screen("budgetNewUpdate_screen")
}