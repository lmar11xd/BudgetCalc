package com.lmar.budgetcalc.feature.data.mapper

import com.lmar.budgetcalc.feature.data.local.dto.LocalMaterial
import com.lmar.budgetcalc.feature.domain.model.Material


fun Material.toLocalMaterial(): LocalMaterial {
    return LocalMaterial(
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        subTotal = subTotal,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        actived = actived,
        budgetId = budgetId,
        id = id
    )
}

fun LocalMaterial.toMaterial(): Material {
    return Material(
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        subTotal = subTotal,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        actived = actived,
        budgetId = budgetId,
        id = id
    )
}

fun List<Material>.toLocalMaterialList(): List<LocalMaterial> {
    return this.map { material ->
        LocalMaterial(
            description = material.description,
            quantity = material.quantity,
            unitPrice = material.unitPrice,
            subTotal = material.subTotal,
            createdAt = material.createdAt,
            modifiedAt = material.modifiedAt,
            actived = material.actived,
            budgetId = material.budgetId,
            id = material.id
        )
    }
}

fun List<LocalMaterial>.toMaterialListFromLocal(): List<Material> {
    return this.map { material ->
        Material(
            description = material.description,
            quantity = material.quantity,
            unitPrice = material.unitPrice,
            subTotal = material.subTotal,
            createdAt = material.createdAt,
            modifiedAt = material.modifiedAt,
            actived = material.actived,
            budgetId = material.budgetId,
            id = material.id
        )
    }
}