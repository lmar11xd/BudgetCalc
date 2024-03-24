package com.lmar.budgetcalc.feature.presentation.budget_new_update

import com.lmar.budgetcalc.feature.domain.model.Budget

data class BudgetNewUpdateState (
    val isTitleHintVisible: Boolean = true,
    val budget: Budget = Budget(
        title = "",
        createdAt = 0,
        modifiedAt = 0,
        actived = false,
        total = 0.0,
        id = null
    ),
    val isLoading: Boolean = true,
    val error: String? = null
)