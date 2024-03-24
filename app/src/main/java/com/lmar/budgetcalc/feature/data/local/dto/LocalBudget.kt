package com.lmar.budgetcalc.feature.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class LocalBudget (
    val title: String,
    val total: Double,
    val createdAt: Long,
    val modifiedAt: Long,
    val actived: Boolean,

    @PrimaryKey(autoGenerate = true)
    val id: Int?
)