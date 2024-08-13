package com.lmar.budgetcalc.feature.domain.use_cases

import com.lmar.budgetcalc.core.util.UseCaseStrings
import com.lmar.budgetcalc.feature.domain.exception.InvalidMaterialException
import com.lmar.budgetcalc.feature.domain.model.Material
import com.lmar.budgetcalc.feature.domain.repo.MaterialRepo
import javax.inject.Inject

class MaterialUseCases @Inject constructor(
    private val materialRepo: MaterialRepo
) {
    suspend fun addAllMaterials(materials: List<Material>) {
        materialRepo.addAllMaterials(materials)
    }

    suspend fun addMaterial(material: Material): Long {
        if(material.description.isBlank()) {
            throw InvalidMaterialException(UseCaseStrings.EMPTY_MATERIAL_DESCRIPTION)
        }
        return materialRepo.addMaterial(material)
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
        val materials = materialRepo.getAllMaterials(budgetId)
        return MaterialUseCaseResult.Success(materials.sortedBy { it.id })
    }

    suspend fun getMaterialsByBudget(budgetId: Int): List<Material> {
        return materialRepo.getAllMaterials(budgetId)
    }

}

sealed class MaterialUseCaseResult {
    data class Success(val materials: List<Material>): MaterialUseCaseResult()
    data class Error(val message: String): MaterialUseCaseResult()
}