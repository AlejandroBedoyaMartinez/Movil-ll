package com.example.marsphotos.dataDivisas

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [divisaEntity::class],
    version = 1
)
@TypeConverters(ConversionRatesConverter::class)
abstract class divisaDb:RoomDatabase() {
    abstract  val dao:divisaDao
}


