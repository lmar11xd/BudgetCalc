package com.lmar.budgetcalc.feature.domain.model

data class Budget (
    val title: String,
    val total: Double,
    val createdAt: Long,
    val modifiedAt: Long,
    val actived: Boolean,
    val id: Int?
)