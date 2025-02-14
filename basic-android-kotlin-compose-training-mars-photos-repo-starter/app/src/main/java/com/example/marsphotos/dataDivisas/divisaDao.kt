package com.example.marsphotos.dataDivisas

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface divisaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(divisaEntity: divisaEntity)

    @Update
    suspend fun update(divisaEntity: divisaEntity)

    @Delete
    suspend fun delete(divisaEntity: divisaEntity)

    @Query("SELECT*FROM divisaEntity")
    fun getAllDivisas():kotlinx.coroutines.flow.Flow<List<divisaEntity>>

    @Query("SELECT * from divisaEntity WHERE id = :id")
    fun getDivisa(id: Int): Flow<divisaEntity>

}