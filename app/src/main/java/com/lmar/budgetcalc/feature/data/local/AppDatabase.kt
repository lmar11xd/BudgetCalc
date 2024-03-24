package com.lmar.budgetcalc.feature.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lmar.budgetcalc.feature.data.local.dto.LocalBudget
import com.lmar.budgetcalc.feature.data.local.dto.LocalMaterial

@Database(
    entities = [LocalBudget::class, LocalMaterial::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract val budgetDao: BudgetDao
    abstract val materialDao: MaterialDao

    companion object {
        const val DATABASE_NAME = "budgetcalc_db"
    }
}