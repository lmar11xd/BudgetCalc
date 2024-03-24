package com.lmar.budgetcalc.feature.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation

data class LocalBudgetMaterials (
    @Embedded val budget: LocalBudget,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val materials: List<LocalMaterial>
)