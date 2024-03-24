package com.lmar.budgetcalc.feature.presentation.budget_list

import com.lmar.budgetcalc.feature.domain.model.Budget

sealed class BudgetListEvent {
    data class Delete(val budget: Budget): BudgetListEvent()
    object UndoDelete: BudgetListEvent()
}