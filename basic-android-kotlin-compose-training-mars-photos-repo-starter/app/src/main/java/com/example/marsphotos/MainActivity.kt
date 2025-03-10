package com.example.marsphotos

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.*
import com.example.marsphotos.ui.DivisasApp
import com.example.marsphotos.ui.theme.MarsPhotosTheme
import com.example.marsphotos.workManager.DivisasWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = applicationContext
        if (!isWorkScheduled(context)) {
            startPeriodicWork(context)
            setWorkScheduled(context)
        }
        setContent {
            MarsPhotosTheme {
                DivisasApp()
            }
        }
    }

    private fun startPeriodicWork(context: Context) {
        Log.d("WorkManager", "Tarea ejecutada: ${System.currentTimeMillis()}")

        val workRequest = OneTimeWorkRequestBuilder<DivisasWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            "divisas_work_once",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
        Log.d("WorkManager", "Trabajo encolado para ejecutarse una vez.")
    }

    fun isWorkScheduled(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("work_manager_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("work_scheduled", false)
    }

    fun setWorkScheduled(context: Context) {
        val sharedPref = context.getSharedPreferences("work_manager_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("work_scheduled", true)
            apply()
        }
    }
}

