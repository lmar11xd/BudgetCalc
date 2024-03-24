package com.lmar.budgetcalc.feature.data.mapper

import com.lmar.budgetcalc.feature.data.local.dto.LocalBudget
import com.lmar.budgetcalc.feature.domain.model.Budget

fun Budget.toLocalBudget(): LocalBudget {
    return LocalBudget(
        title = title,
        total = total,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        actived = actived,
        id = id
    )
}

fun LocalBudget.toBudget(): Budget {
    return Budget(
        title = title,
        total = total,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        actived = actived,
        id = id
    )
}

fun List<Budget>.toLocalBudgetList(): List<LocalBudget> {
    return this.map { budget ->
        LocalBudget(
            title = budget.title,
            total = budget.total,
            createdAt = budget.createdAt,
            modifiedAt = budget.modifiedAt,
            actived = budget.actived,
            id = budget.id
        )
    }
}

fun List<LocalBudget>.toBudgetListFromLocal(): List<Budget> {
    return this.map { budget ->
        Budget(
            title = budget.title,
            total = budget.total,
            createdAt = budget.createdAt,
            modifiedAt = budget.modifiedAt,
            actived = budget.actived,
            id = budget.id
        )
    }
}