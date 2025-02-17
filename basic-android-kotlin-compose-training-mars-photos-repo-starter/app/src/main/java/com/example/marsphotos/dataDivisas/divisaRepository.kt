package com.example.marsphotos.dataDivisas

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class divisaRepository (private val divisaDao: divisaDao
) {
    fun getDivisas(): Flow<List<Divisa>> {
        return divisaDao.getAllDivisas()
            .map { entities ->
                entities.map { entity ->
                    Divisa(
                        id = entity.id,
                        base_code = entity.base_code,
                        conversion_rates = entity.conversion_rates,
                        Date = entity.Date
                    )
                }
            }
    }

    fun getDivisa(id: Int): Flow<Divisa> {
        return divisaDao.getDivisa(id)
            .map { entity ->
                Divisa(
                    id = entity.id,
                    base_code = entity.base_code,
                    conversion_rates = entity.conversion_rates,
                    Date = entity.Date
                )
            }
    }


    suspend fun insertDivisa(entity: Divisa){
        val entity = divisaEntity(
            id = entity.id,
            base_code = entity.base_code,
            conversion_rates = entity.conversion_rates,
            Date = entity.Date
        )
        divisaDao.insert(entity)
    }

    suspend fun deleteDivisa(entity: Divisa){
        val entity = divisaEntity(
            id = entity.id,
            base_code = entity.base_code,
            conversion_rates = entity.conversion_rates,
            Date = entity.Date
        )
        divisaDao.delete(entity)
    }

    suspend fun editDivisa(entity: Divisa){
        val entity = divisaEntity(
            id = entity.id,
            base_code = entity.base_code,
            conversion_rates = entity.conversion_rates,
            Date = entity.Date
        )
        divisaDao.update(entity)
    }

}