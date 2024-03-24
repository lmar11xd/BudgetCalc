package com.lmar.budgetcalc.feature.presentation.budget_new_update

import androidx.compose.ui.focus.FocusState

sealed class BudgetNewUpdateEvent {
    data class EnteredTitle(val value: String): BudgetNewUpdateEvent()
    data class ChangedTitleFocus(val focusState: FocusState): BudgetNewUpdateEvent()
    object Delete: BudgetNewUpdateEvent()
    object SaveTodo: BudgetNewUpdateEvent()
    object Back: BudgetNewUpdateEvent()
}
