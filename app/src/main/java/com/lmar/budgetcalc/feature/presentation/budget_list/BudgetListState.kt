package com.lmar.budgetcalc.feature.presentation.budget_list

import com.lmar.budgetcalc.feature.domain.model.Budget

data class BudgetListState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)