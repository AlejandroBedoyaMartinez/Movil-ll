package com.example.marsphotos.data

import com.example.marsphotos.network.DivisasApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/e3ce342fafdb05301b1ad31b/latest/MXN"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideDivisasApiService(retrofit: Retrofit): DivisasApiService {
        return retrofit.create(DivisasApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDivisasRepository(apiService: DivisasApiService): DivisasRepository {
        return NetworkDivisasRepository(apiService)
    }
}

