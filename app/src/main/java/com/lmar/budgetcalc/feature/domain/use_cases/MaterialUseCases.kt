package com.lmar.budgetcalc.feature.domain.use_cases

import com.lmar.budgetcalc.core.util.UseCaseStrings
import com.lmar.budgetcalc.feature.domain.exception.InvalidMaterialException
import com.lmar.budgetcalc.feature.domain.model.Material
import com.lmar.budgetcalc.feature.domain.repo.MaterialRepo
import javax.inject.Inject

class MaterialUseCases @Inject constructor(
    private val materialRepo: MaterialRepo
) {
    suspend fun addMaterial(material: Material) {
        if(material.description.isBlank()) {
            throw InvalidMaterialException(UseCaseStrings.EMPTY_MATERIAL_DESCRIPTION)
        }
        materialRepo.addMaterial(material)
    }

    suspend fun updateMaterial(material: Material) {
        if(material.description.isBlank()) {
            throw InvalidMaterialException(UseCaseStrings.EMPTY_MATERIAL_DESCRIPTION)
        }
        materialRepo.updateMaterial(material)
    }

    suspend fun deleteMaterial(material: Material) {
        materialRepo.deleteMaterial(material)
    }

    suspend fun getMaterials(budgetId: Int): MaterialUseCaseResult {
        var materials = materialRepo.getAllMaterials(budgetId)
        return MaterialUseCaseResult.Success(materials.sortedBy { it.id })
    }

}

sealed class MaterialUseCaseResult {
    data class Success(val budgets: List<Material>): MaterialUseCaseResult()
    data class Error(val message: String): MaterialUseCaseResult()
}