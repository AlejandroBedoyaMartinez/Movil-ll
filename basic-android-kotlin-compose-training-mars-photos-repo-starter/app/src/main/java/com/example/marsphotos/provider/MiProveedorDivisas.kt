package com.example.marsphotos.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.content.Context
import com.example.marsphotos.data.ContentProviderEntryPoint
import com.example.marsphotos.dataDivisas.Divisa
import com.example.marsphotos.dataDivisas.divisaRepository
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MiProveedorDivisas : ContentProvider() {

    private lateinit var repository: divisaRepository

    override fun onCreate(): Boolean {
        val appContext = context?.applicationContext ?: return false

        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            ContentProviderEntryPoint::class.java
        )
        repository = hiltEntryPoint.repository()

        return true
    }

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.divisas.provider", "divisas", 1)
        addURI("com.example.divisas.provider", "divisas/currency/*", 2)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("base_code", "currency", "rate", "date"))

        when (sUriMatcher.match(uri)) {
            1 -> {
                val divisas: List<Divisa> = runBlocking {
                    repository.getDivisas().first()
                }
                divisas.forEach { divisa ->
                    divisa.conversion_rates.forEach { (currency, rate) ->
                        cursor.addRow(arrayOf(divisa.base_code, currency, rate, divisa.Date))
                    }
                }
            }
            2 -> {
                if (selectionArgs == null || selectionArgs.size < 3) return null
                val moneda = selectionArgs[0]
                val fechaInicio = selectionArgs[1]
                val fechaFin = selectionArgs[2]

                val divisas: List<Divisa> = runBlocking {
                    repository.getDivisasByDateRange(fechaInicio, fechaFin).first()
                }

                divisas.forEach { divisa ->
                    val rate = divisa.conversion_rates[moneda] ?: return@forEach
                    cursor.addRow(arrayOf(divisa.base_code, moneda, rate, divisa.Date))
                }
            }
            else -> return null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/vnd.com.example.divisas.provider.divisas"
            2 -> "vnd.android.cursor.item/vnd.com.example.divisas.provider.divisas"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
