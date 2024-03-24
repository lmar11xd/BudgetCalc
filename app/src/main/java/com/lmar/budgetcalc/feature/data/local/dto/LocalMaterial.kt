package com.lmar.budgetcalc.feature.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material")
data class LocalMaterial (
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val subTotal: Double,
    val createdAt: Long,
    val modifiedAt: Long,
    val actived: Boolean,

    val budgetId: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int?
)