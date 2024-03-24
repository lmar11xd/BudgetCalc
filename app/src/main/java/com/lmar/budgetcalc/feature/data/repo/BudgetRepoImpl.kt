package com.lmar.budgetcalc.feature.data.repo

import com.lmar.budgetcalc.feature.data.di.IoDispatcher
import com.lmar.budgetcalc.feature.data.local.BudgetDao
import com.lmar.budgetcalc.feature.data.mapper.toBudget
import com.lmar.budgetcalc.feature.data.mapper.toBudgetListFromLocal
import com.lmar.budgetcalc.feature.data.mapper.toLocalBudget
import com.lmar.budgetcalc.feature.domain.model.Budget
import com.lmar.budgetcalc.feature.domain.repo.BudgetRepo
import kotlinx.coroutines.CoroutineDispatcher

class BudgetRepoImpl(
    private val budgetDao: BudgetDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): BudgetRepo {
    override suspend fun getAllBudgets(): List<Budget> {
        //getAllBudgetsFromRemote()
        return budgetDao.getAllBudgets().toBudgetListFromLocal()
    }

    override suspend fun getAllBudgetsFromLocal(): List<Budget> {
        return budgetDao.getAllBudgets().toBudgetListFromLocal()
    }

    override suspend fun getAllBudgetsFromRemote() {

    }

    override suspend fun getBudgetById(id: Int): Budget? {
        return budgetDao.getBudget(id)?.toBudget()
    }

    override suspend fun addBudget(budget: Budget): Long {
        return budgetDao.addBudget(budget.toLocalBudget())
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget.toLocalBudget())
    }

    override suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget.toLocalBudget())
    }

    private suspend fun refreshRoomCache() {
        //val remoteBudgets = api.getAllBudgets().filterNotNull()
        //budgetDao.addAllTodoItems(remoteBudgets.toLocalBudgetListFromRemote())
    }

    private fun isCacheEmpty(): Boolean {
        var empty = true
        if(budgetDao.getAllBudgets().isNotEmpty()) empty = false
        return empty
    }
}