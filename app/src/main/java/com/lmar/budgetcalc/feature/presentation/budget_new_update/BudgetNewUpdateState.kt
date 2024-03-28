package com.lmar.budgetcalc.feature.presentation.budget_new_update

import com.lmar.budgetcalc.feature.domain.model.Budget
import com.lmar.budgetcalc.feature.domain.model.Material

data class BudgetNewUpdateState (
    val isTitleHintVisible: Boolean = true,
    val budget: Budget = Budget(
        title = "",
        createdAt = 0,
        modifiedAt = 0,
        actived = true,
        total = 0.0,
        id = null
    ),
    val materials: List<Material> = emptyList(),
    val descriptionMaterial: String = "",
    val quantityMaterial: String = "",
    val unitPriceMaterial: String = "",
    val showMaterialDialog: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)