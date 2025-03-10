package com.example.marsphotos.dataDivisas

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConversionRatesConverter
{
    @TypeConverter
    fun fromMap(value: Map<String, Double>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMap(value: String): Map<String, Double> {
        return Json.decodeFromString(value)
    }
}