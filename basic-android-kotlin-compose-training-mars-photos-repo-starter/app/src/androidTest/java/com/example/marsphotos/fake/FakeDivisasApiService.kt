package com.example.marsphotos.fake

import com.example.marsphotos.model.divisas
import com.example.marsphotos.network.DivisasApiService

class FakeDivisasApiService : DivisasApiService {

    override suspend fun getPhotos(): List<divisas> {
        return FakeDataSource.photosList
    }

}
