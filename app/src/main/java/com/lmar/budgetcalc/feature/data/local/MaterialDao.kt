package com.lmar.budgetcalc.feature.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lmar.budgetcalc.feature.data.local.dto.LocalMaterial

@Dao
interface MaterialDao {
    @Query("SELECT * FROM material WHERE budgetId = :budgetId")
    fun getAllMaterials(budgetId: Int): List<LocalMaterial>

    @Query("SELECT * FROM material WHERE id = :id")
    suspend fun getMaterial(id: Int): LocalMaterial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMaterials(materials: List<LocalMaterial>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMaterial(material: LocalMaterial): Long

    @Update
    suspend fun updateMaterial(material: LocalMaterial)

    @Delete
    suspend fun deleteMaterial(material: LocalMaterial)
}