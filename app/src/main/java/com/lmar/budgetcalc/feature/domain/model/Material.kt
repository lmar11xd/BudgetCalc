package com.lmar.budgetcalc.feature.domain.model

data class Material(
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val subTotal: Double,
    val createdAt: Long,
    val modifiedAt: Long,
    val actived: Boolean,
    val budgetId: Int,
    val id: Int?
)
