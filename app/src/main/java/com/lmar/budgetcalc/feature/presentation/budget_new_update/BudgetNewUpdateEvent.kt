package com.lmar.budgetcalc.feature.presentation.budget_new_update

import androidx.compose.ui.focus.FocusState

sealed class BudgetNewUpdateEvent {
    data class EnteredTitle(val value: String): BudgetNewUpdateEvent()
    data class ChangedTitleFocus(val focusState: FocusState): BudgetNewUpdateEvent()
    data class ChangeShowMaterialDialog(val show: Boolean): BudgetNewUpdateEvent()
    object Delete: BudgetNewUpdateEvent()
    object Save: BudgetNewUpdateEvent()
    object Back: BudgetNewUpdateEvent()
    data class EnteredDescriptionMaterial(val value: String): BudgetNewUpdateEvent()
    data class EnteredQuantityMaterial(val value: String): BudgetNewUpdateEvent()
    data class EnteredUnitPriceMaterial(val value: String): BudgetNewUpdateEvent()
    object AddMaterial: BudgetNewUpdateEvent()
}
