package com.lmar.budgetcalc.feature.presentation.budget_new_update

import androidx.compose.ui.focus.FocusState
import com.lmar.budgetcalc.feature.domain.model.Material

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
    data class DeleteMaterial(val material: Material): BudgetNewUpdateEvent()
    object AddMaterial: BudgetNewUpdateEvent()
}
