package com.example.marsphotos.fake

import com.example.marsphotos.data.NetworkDivisasRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkMarsRepositoryTest {
    @Test
    fun networkMarsPhotosRepository_getMarsPhotos_verifyPhotoList(): Unit =
        runTest {
            val repository = NetworkDivisasRepository(
                divisasApiService = FakeDivisasApiService()
            )
            assertEquals(FakeDataSource.photosList, repository.getDivisas())
        }


}