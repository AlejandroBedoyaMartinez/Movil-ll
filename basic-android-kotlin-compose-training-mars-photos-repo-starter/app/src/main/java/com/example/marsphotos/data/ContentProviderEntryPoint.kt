package com.example.marsphotos.data

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.marsphotos.dataDivisas.divisaRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ContentProviderEntryPoint {
    fun repository(): divisaRepository
}