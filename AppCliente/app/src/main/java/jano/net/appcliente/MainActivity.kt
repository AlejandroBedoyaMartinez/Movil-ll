package jano.net.appcliente

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import jano.net.appcliente.ui.theme.AppClienteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppClienteTheme {
                ConsultaDivisas()
            }
        }
    }
}

@Composable
fun ConsultaDivisas() {
    val context = LocalContext.current
    val resultado = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        resultado.value = obtenerDatosDelProvider(context)
    }

    Log.d("Cliente", resultado.value)
    Text(
        text = resultado.value,
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    )
}

fun obtenerDatosDelProvider(context: Context): String {
    val cursor = context.contentResolver.query(
        Uri.parse("content://provider/divisas"),
        null, null, null, null
    )

    val resultado = StringBuilder()
    if (cursor != null && cursor.moveToFirst()) {
        do {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val baseCode = cursor.getString(cursor.getColumnIndexOrThrow("base_code"))
            val conversionRates = cursor.getString(cursor.getColumnIndexOrThrow("conversion_rates"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("Date"))

            resultado.append("ID: $id, Base Code: $baseCode, Conversion Rates: $conversionRates, Date: $date\n")
        } while (cursor.moveToNext())

        cursor.close()
    } else {
        resultado.append("Cursor vacío o no se pudo obtener datos.\n")
    }

    return resultado.toString()
}



