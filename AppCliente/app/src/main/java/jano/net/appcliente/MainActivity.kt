package jano.net.appcliente

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import androidx.compose.ui.viewinterop.AndroidView
import jano.net.appcliente.ui.theme.AppClienteTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppClienteTheme {
                GraficoTodasLasDivisas()
            }
        }
    }
}

@Composable
fun GraficoTodasLasDivisas() {
    val context = LocalContext.current
    val datos = remember { mutableStateOf<List<BarEntry>>(emptyList()) }
    val labels = remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        val (entries, currencyLabels) = obtenerDatosParaGrafico(context)
        datos.value = entries
        labels.value = currencyLabels
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp),
        factory = { ctx ->
            BarChart(ctx).apply {
                description = Description().apply { text = "Conversiones de MXN" }
                xAxis.labelRotationAngle = 90f // Rotar etiquetas para mejor lectura
                axisRight.isEnabled = false // Ocultar eje derecho
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(datos.value, "Valor en otras monedas").apply {
                color = android.graphics.Color.BLUE
                valueTextSize = 8f
            }
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    )
}

fun obtenerDatosParaGrafico(context: Context): Pair<List<BarEntry>, List<String>> {
    val cursor = context.contentResolver.query(
        Uri.parse("content://provider/divisas"),
        null, null, null, null
    )

    val listaDatos = mutableListOf<BarEntry>()
    val labels = mutableListOf<String>()

    if (cursor != null && cursor.moveToFirst()) {
        val conversionRates = cursor.getString(cursor.getColumnIndexOrThrow("conversion_rates"))
        val jsonRates = JSONObject(conversionRates)
        var index = 0f

        jsonRates.keys().forEach { currencyCode ->
            val rate = jsonRates.optDouble(currencyCode, Double.NaN)
            if (!rate.isNaN()) {
                listaDatos.add(BarEntry(index, rate.toFloat()))
                labels.add(currencyCode)
                index++
            }
        }

        cursor.close()
    }
    return Pair(listaDatos, labels)
}
