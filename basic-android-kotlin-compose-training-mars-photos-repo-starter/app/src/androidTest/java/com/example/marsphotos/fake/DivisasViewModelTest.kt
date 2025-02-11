package com.example.marsphotos.fake

import com.example.marsphotos.fake.rules.TestDispatcherRule
import com.example.marsphotos.ui.screens.DivisasUiState
import com.example.marsphotos.ui.screens.DivisasViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DivisasViewModelTest {
    
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun marsViewModel_getMarsPhotos_verifyMarsUiStateSuccess(): Unit =
        runTest {
            val divisasViewModel = DivisasViewModel(
                divisasRepository = FakeNetworkDivisasRepository()
            )
            assertEquals(
                DivisasUiState.Success("Success: ${FakeDataSource.photosList.size} Mars " +
                        "photos retrieved"),
                divisasViewModel.divisasUiState
            )
        }

}