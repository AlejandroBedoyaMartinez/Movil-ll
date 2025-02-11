package com.example.marsphotos.data

import com.example.marsphotos.model.divisas
import com.example.marsphotos.network.DivisasApiService


interface DivisasRepository {
    suspend fun getDivisas(): List<divisas>
}

class NetworkDivisasRepository(
    private val divisasApiService: DivisasApiService
) : DivisasRepository {
    override suspend fun getDivisas(): List<divisas> = divisasApiService.getPhotos()
}


