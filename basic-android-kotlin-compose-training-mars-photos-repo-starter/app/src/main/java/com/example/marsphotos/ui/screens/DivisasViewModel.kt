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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.data.DivisasRepository
import com.example.marsphotos.model.divisas
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val divisasRepository: DivisasRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var divisasUiState: DivisasUiState by mutableStateOf(DivisasUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getDivisas()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [divisas] [List] [MutableList].
     */
    fun getDivisas() {
        viewModelScope.launch {
            divisasUiState = DivisasUiState.Loading
            divisasUiState = try {
                val listDivisas = divisasRepository.getDivisas()
                DivisasUiState.Success(
                    "Base: ${listDivisas.base_code}, " +
                            "Tasa USD: ${listDivisas.conversion_rates["USD"]}"
                )
            } catch (e: IOException) {
                DivisasUiState.Error
            } catch (e: HttpException) {
                DivisasUiState.Error
            }
        }
    }
}