package com.example.marsphotos.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
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
        Log.d("MiProveedorDivisas", "Proveedor creado correctamente.")
        return true
    }

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("provider", "divisas", 1)

    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("id","base_code", "conversion_rates", "Date"))
        Log.d("MiProveedorDivisas", "entrando a querys ")
        val match = sUriMatcher.match(uri)
        Log.d("MiProveedorDivisas", "URI match result: $match, URI: $uri")
        if (match != 1) {
            Log.e("MiProveedorDivisas", "URI no reconocida en sUriMatcher")
            return null
        }
        when (sUriMatcher.match(uri)) {
            1 -> {
                val divisas: List<Divisa> = runBlocking {
                    repository.getDivisas().first()
                }

                if (divisas.isEmpty()) {
                    Log.d("MiProveedorDivisas", "No se encontraron divisas")
                } else {
                    divisas.forEach { divisa ->
                        cursor.addRow(arrayOf(divisa.id,divisa.base_code, divisa.conversion_rates, divisa.Date))
                    }
                }
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/vnd.provider.divisas"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
