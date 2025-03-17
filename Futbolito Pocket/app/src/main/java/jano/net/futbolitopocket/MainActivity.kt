package jano.net.futbolitopocket

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import dev.ricknout.composesensors.getSensorManager
import jano.net.futbolitopocket.ui.theme.FutbolitoPocketTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FutbolitoPocketTheme {
                    FutbolitoPocket()
            }
        }
    }
}

@Composable
fun FutbolitoPocket() {
    val sensorManager = getSensorManager()
    val available = isAccelerometerSensorAvailable()
    val sensorValue by rememberAccelerometerSensorValueAsState()
    val (x, y, z) = sensorValue.value

    var showDialog by remember { mutableStateOf(false) }

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.balonfutbol)

    var golesEquipoA by remember { mutableStateOf(0) }
    var golesEquipoB by remember { mutableStateOf(0) }

    var velocidad:Float = 10f

    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() - 150}
    val screenHeight = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    var offsetX by remember { mutableStateOf(screenWidth / 2)}
    var offsetY by remember { mutableStateOf(screenHeight / 2 )}
    var gol by remember { mutableStateOf(true) }
    LaunchedEffect(sensorValue) {
        while (gol) {
            if (offsetX < 50) offsetX = 100f
            if (offsetX > screenWidth) offsetX = screenWidth - 50

            if (offsetX > 420f && offsetX < 570f && offsetY < 300f) {
                golesEquipoB++
                gol = false
                showDialog = true
            }else if ((offsetX < 420f || offsetX > 570f) && offsetY < 370){
                offsetY = 420f
            }

            if (offsetX > 420f && offsetX < 570f && offsetY > 1800f) {
                golesEquipoA++
                gol = false
                showDialog = true
            }else if ((offsetX < 420f || offsetX > 570f) && offsetY > 1740) {
                offsetY = 1690f
            }

            offsetX += (-x) * velocidad
            offsetY += y * velocidad

            delay(16L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Equipo A $golesEquipoA - $golesEquipoB Equipo B",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.canchafutbol),
            contentDescription = "Fondo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )

        Canvas(modifier = Modifier.matchParentSize()) {
            withTransform({
                scale(scaleX = 0.5f, scaleY = 0.5f, pivot = Offset(offsetX, offsetY))
            }) {
                drawImage(
                    image = imageBitmap,
                    topLeft = Offset(offsetX, offsetY)
                )
            }
        }

        if (showDialog) {
            GolDialogo(
                golesEquipoA = golesEquipoA,
                golesEquipoB = golesEquipoB,
                onDismiss = {
                    showDialog = false
                    offsetX = screenWidth / 2
                    offsetY = screenHeight / 2
                    gol = true
                }
            )
        }
    }
}



@Composable
fun GolDialogo(golesEquipoA: Int, golesEquipoB: Int, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¡Gol!") },
        text = {
            Text("Equipo A: $golesEquipoA\nEquipo B: $golesEquipoB")
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}