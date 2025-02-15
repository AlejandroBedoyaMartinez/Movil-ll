package com.example.marsphotos.workManager

import android.util.Log
import com.example.marsphotos.data.DivisasRepository
import com.example.marsphotos.dataDivisas.Divisa
import com.example.marsphotos.dataDivisas.divisaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class DivisasWorkerFactory @Inject constructor(
    private val db: divisaRepository,
    private val divisasRepository: DivisasRepository
) {

    suspend fun insertarDivisa() {
        try {
            val listDivisas = divisasRepository.getDivisas()
            val entity = Divisa(
                base_code = listDivisas.base_code,
                conversion_rates = listDivisas.conversion_rates
            )
            Log.d("WorkManager", "Divisa insertada correctamente en la BD.")
            db.insertDivisa(entity)
        } catch (e: Exception) {
            Log.e("WorkManager", "Error insertando divisa: ${e.message}")
        }
    }

    suspend fun getDivisasSize(): Int {
        val divisasList = db.getDivisas().first()
        return divisasList.size
    }
}

