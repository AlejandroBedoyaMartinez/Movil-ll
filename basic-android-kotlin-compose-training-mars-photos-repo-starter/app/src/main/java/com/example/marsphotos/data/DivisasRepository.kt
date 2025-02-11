package com.example.marsphotos.data

import com.example.marsphotos.model.divisas
import com.example.marsphotos.network.DivisasApiService


interface DivisasRepository {
    suspend fun getDivisas(): divisas
}

class NetworkDivisasRepository(
    private val divisasApiService: DivisasApiService
) : DivisasRepository {
    override suspend fun getDivisas(): divisas = divisasApiService.getDivisas()
}


