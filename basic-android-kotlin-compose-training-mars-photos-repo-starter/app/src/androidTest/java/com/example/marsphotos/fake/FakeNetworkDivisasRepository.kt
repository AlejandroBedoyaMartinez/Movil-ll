package com.example.marsphotos.fake

import com.example.marsphotos.data.DivisasRepository
import com.example.marsphotos.model.divisas

class FakeNetworkDivisasRepository : DivisasRepository {
    override suspend fun getDivisas(): List<divisas> {
        return FakeDataSource.photosList
    }
}
