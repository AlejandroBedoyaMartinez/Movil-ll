package jano.net.appcliente

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import jano.net.appcliente.ui.theme.AppClienteTheme
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppClienteTheme {
                PantallaConsulta()
            }
        }
    }
}

@Composable
fun PantallaConsulta() {
    val context = LocalContext.current
    var moneda by remember { mutableStateOf("MXN") }
    var fechaInicio by remember { mutableStateOf("2025-01-01") }
    var fechaFin by remember { mutableStateOf("2025-12-31") }
    val datos = remember { mutableStateOf<List<BarEntry>>(emptyList()) }
    val labels = remember { mutableStateOf<List<String>>(emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = moneda,
            onValueChange = { moneda = it.uppercase() },
            label = { Text("Código de moneda") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = fechaInicio,
            onValueChange = { fechaInicio = it },
            label = { Text("Fecha de inicio (YYYY-MM-DD)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = fechaFin,
            onValueChange = { fechaFin = it },
            label = { Text("Fecha de fin (YYYY-MM-DD)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val (entries, currencyLabels) = obtenerDatosParaGrafico(context, moneda, fechaInicio, fechaFin)
            datos.value = entries
            labels.value = currencyLabels
        }) {
            Text("Consultar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        GraficoDivisas(datos.value, labels.value)
    }
}

@Composable
fun GraficoDivisas(datos: List<BarEntry>, labels: List<String>) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawLabels(true)
                xAxis.granularity = 1f

                axisRight.isEnabled = false
                axisLeft.valueFormatter = LargeValueFormatter()
                setDrawValueAboveBar(true)
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(datos, "Valor en otras monedas").apply {
                color = android.graphics.Color.GRAY
                valueTextSize = 12f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "$value"
                    }
                }
            }
            chart.xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return labels.getOrNull(index) ?: ""
                    }
                }
            }
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    )
}




fun obtenerDatosParaGrafico(context: Context, moneda: String, fechaInicio: String, fechaFin: String): Pair<List<BarEntry>, List<String>> {
    val uri = Uri.parse("content://provider/divisas/$fechaInicio/$fechaFin/$moneda")
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    val listaDatos = mutableListOf<BarEntry>()
    val labels = mutableListOf<String>()

    if (cursor != null && cursor.moveToFirst()) {
        val indexConversionRate = cursor.getColumnIndex("conversion_rate")
        val indexDate = cursor.getColumnIndex("date")

        var index = 0f
        do {
            val rate = cursor.getDouble(indexConversionRate)
            val date = cursor.getString(indexDate)

            listaDatos.add(BarEntry(index, rate.toFloat()))
            labels.add(date)
            index++
        } while (cursor.moveToNext())

        cursor.close()
    }
    return Pair(listaDatos, labels)
}
