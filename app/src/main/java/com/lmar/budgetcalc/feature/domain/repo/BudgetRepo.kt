package com.lmar.budgetcalc.feature.domain.repo

import com.lmar.budgetcalc.feature.domain.model.Budget

interface BudgetRepo {
    suspend fun getAllBudgets(): List<Budget>
    suspend fun getAllBudgetsFromLocal(): List<Budget>
    suspend fun getAllBudgetsFromRemote()
    suspend fun getBudgetById(id: Int): Budget?
    suspend fun addBudget(budget: Budget): Long
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
}