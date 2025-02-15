package com.example.marsphotos.workManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class DivisasWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val divisasWorkerFactory: DivisasWorkerFactory
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            divisasWorkerFactory.insertarDivisa()
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
        Log.d("WorkManager", "Tarea nueva: ${System.currentTimeMillis()}")
        val workRequest = OneTimeWorkRequestBuilder<DivisasWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
