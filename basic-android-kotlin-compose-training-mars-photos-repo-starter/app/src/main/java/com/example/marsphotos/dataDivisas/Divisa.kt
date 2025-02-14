package com.example.marsphotos.dataDivisas

import androidx.room.PrimaryKey
import androidx.room.TypeConverters

data class Divisa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val base_code: String,
    @TypeConverters(ConversionRatesConverter::class)
    val conversion_rates: Map<String, Double>
)
