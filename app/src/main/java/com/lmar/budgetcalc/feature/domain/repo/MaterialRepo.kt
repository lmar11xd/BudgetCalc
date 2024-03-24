package com.lmar.budgetcalc.feature.domain.repo

import com.lmar.budgetcalc.feature.domain.model.Material

interface MaterialRepo {
    suspend fun getAllMaterials(budgetId: Int): List<Material>
    suspend fun getAllMaterialsFromLocal(budgetId: Int): List<Material>
    suspend fun getAllMaterialsFromRemote()
    suspend fun getMaterialById(id: Int): Material?
    suspend fun addMaterial(material: Material)
    suspend fun updateMaterial(material: Material)
    suspend fun deleteMaterial(material: Material)
}