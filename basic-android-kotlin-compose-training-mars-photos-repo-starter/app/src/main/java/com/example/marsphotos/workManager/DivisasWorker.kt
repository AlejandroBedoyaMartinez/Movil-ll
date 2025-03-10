package com.example.marsphotos.workManager

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@HiltWorker
class DivisasWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val divisasWorkerFactory: DivisasWorkerFactory
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val fechaHoraActual = LocalDateTime.now()
            divisasWorkerFactory.insertarDivisa(fechaHoraActual.format(formato).toString())
            Log.d("WorkManager", "Tarea Finalizada: ${System.currentTimeMillis()}")
            if(divisasWorkerFactory.getDivisasSize() < 3){
                scheduleNextWork()
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "Error en DivisasWorker: ${e.message}")
            Result.failure()
        }
    }
    private fun scheduleNextWork() {
        Log.d("WorkManager", "Tarea nueva: ${System.currentTimeMillis()/60000}")
        val workRequest = OneTimeWorkRequestBuilder<DivisasWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
