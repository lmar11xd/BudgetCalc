package com.lmar.budgetcalc.feature.data.repo

import com.lmar.budgetcalc.feature.data.di.IoDispatcher
import com.lmar.budgetcalc.feature.data.mapper.toLocalMaterial
import com.lmar.budgetcalc.feature.data.mapper.toMaterial
import com.lmar.budgetcalc.feature.data.mapper.toMaterialListFromLocal
import com.lmar.budgetcalc.feature.domain.model.Material
import com.lmar.budgetcalc.feature.domain.repo.MaterialRepo
import com.lmar.budgetcalc.feature.data.local.MaterialDao
import com.lmar.budgetcalc.feature.data.mapper.toLocalMaterialList
import kotlinx.coroutines.CoroutineDispatcher

class MaterialRepoImpl(
    private val materialDao: MaterialDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): MaterialRepo {
    override suspend fun getAllMaterials(budgetId: Int): List<Material> {
        return materialDao.getAllMaterials(budgetId).toMaterialListFromLocal()
    }

    override suspend fun getAllMaterialsFromLocal(budgetId: Int): List<Material> {
        return materialDao.getAllMaterials(budgetId).toMaterialListFromLocal()
    }

    override suspend fun getAllMaterialsFromRemote() {
    }

    override suspend fun getMaterialById(id: Int): Material? {
        return materialDao.getMaterial(id)?.toMaterial()
    }

    override suspend fun addAllMaterials(materials: List<Material>) {
        materialDao.addAllMaterials(materials.toLocalMaterialList())
    }

    override suspend fun addMaterial(material: Material): Long {
        return materialDao.addMaterial(material.toLocalMaterial())
    }

    override suspend fun updateMaterial(material: Material) {
        materialDao.updateMaterial(material.toLocalMaterial())
    }

    override suspend fun deleteMaterial(material: Material) {
        materialDao.deleteMaterial(material.toLocalMaterial())
    }
}