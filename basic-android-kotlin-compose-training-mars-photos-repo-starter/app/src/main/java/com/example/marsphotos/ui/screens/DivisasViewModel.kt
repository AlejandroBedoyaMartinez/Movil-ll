/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.marsphotos.data.DivisasRepository
import com.example.marsphotos.dataDivisas.Divisa
import com.example.marsphotos.dataDivisas.divisaRepository
import com.example.marsphotos.model.divisas
import com.example.marsphotos.workManager.DivisasWorker
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * UI state for the Home screen
 */
sealed interface DivisasUiState {
    data class Success(val photos: String) : DivisasUiState
    object Error : DivisasUiState
    object Loading : DivisasUiState
}


@HiltViewModel
class DivisasViewModel @Inject constructor(
    private val divisasRepository: DivisasRepository,
    private val Db: divisaRepository
) : ViewModel() {

    /** The mutable State that stores the status of the most recent request */
    var divisasUiState: DivisasUiState by mutableStateOf(DivisasUiState.Loading)
        private set
    private val _Divisa: MutableStateFlow<List<Divisa>> = MutableStateFlow(emptyList())
    var divisas: StateFlow<List<Divisa>> = _Divisa
    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getDivisas()
    }

    init {
        viewModelScope.launch {
            Db.getDivisas().collect { divisaList ->
                _Divisa.value = divisaList
            }
        }
    }



    fun getDivisas() {
        viewModelScope.launch {
            divisasUiState = DivisasUiState.Loading
            divisasUiState = try {
                val listDivisas = divisasRepository.getDivisas()
                //insertarDivisaDB(listDivisas)
                DivisasUiState.Success(
                    "Base: ${listDivisas.base_code}, " +
                            "Tasa USD: ${listDivisas.conversion_rates["USD"]}\n" +
                            "Total de actualizaciones: ${divisas.value.size}"
                )

            } catch (e: IOException) {
                DivisasUiState.Error
            } catch (e: HttpException) {
                DivisasUiState.Error
            }
        }
    }

    suspend fun insertarDivisaDB(){
        val listDivisas = divisasRepository.getDivisas()
        val entity = Divisa(
            base_code = listDivisas.base_code,
            conversion_rates = listDivisas.conversion_rates
        )
        viewModelScope.launch {
            Db.insertDivisa(entity)
        }
    }

}