package com.lmar.budgetcalc.feature.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lmar.budgetcalc.feature.data.local.dto.LocalBudget
import com.lmar.budgetcalc.feature.data.local.dto.LocalBudgetMaterials

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget")
    fun getAllBudgets(): List<LocalBudget>

    @Query("SELECT * FROM budget WHERE id = :id")
    suspend fun getBudget(id: Int): LocalBudget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllBudgets(budgets: List<LocalBudget>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBudget(budget: LocalBudget): Long

    @Update
    suspend fun updateBudget(budget: LocalBudget)

    @Delete
    suspend fun deleteBudget(budget: LocalBudget)

    @Transaction
    @Query("SELECT * FROM budget WHERE id = :id")
    fun getAllMaterialsByBudget(id: Int): List<LocalBudgetMaterials>
}