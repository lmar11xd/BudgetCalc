package com.lmar.budgetcalc.feature.domain.use_cases

import com.lmar.budgetcalc.core.util.UseCaseStrings
import com.lmar.budgetcalc.feature.domain.exception.InvalidBudgetException
import com.lmar.budgetcalc.feature.domain.model.Budget
import com.lmar.budgetcalc.feature.domain.repo.BudgetRepo
import javax.inject.Inject

class BudgetUseCases @Inject constructor(
    private val budgetRepo: BudgetRepo
) {
    suspend fun addBudget(budget: Budget): Long {
        if(budget.title.isBlank()) {
            throw InvalidBudgetException(UseCaseStrings.EMPTY_BUDGET_TITLE)
        }
        return budgetRepo.addBudget(budget)
    }

    suspend fun updateBudget(budget: Budget) {
        if(budget.title.isBlank()) {
            throw InvalidBudgetException(UseCaseStrings.EMPTY_BUDGET_TITLE)
        }
        budgetRepo.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: Budget) {
        budgetRepo.deleteBudget(budget)
    }

    suspend fun getBudgetById(id: Int): Budget? {
        return budgetRepo.getBudgetById(id)
    }

    suspend fun getBudgets(): BudgetUseCaseResult {
        var budgets = budgetRepo.getAllBudgets()
        return BudgetUseCaseResult.Success(budgets.sortedByDescending { it.modifiedAt })
    }
}

sealed class BudgetUseCaseResult {
    data class Success(val budgets: List<Budget>): BudgetUseCaseResult()
    data class Error(val message: String): BudgetUseCaseResult()
}