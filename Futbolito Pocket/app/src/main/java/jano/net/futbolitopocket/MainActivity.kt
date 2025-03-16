package jano.net.futbolitopocket

import android.hardware.Sensor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ricknout.composesensors.getSensor
import dev.ricknout.composesensors.getSensorManager
import dev.ricknout.composesensors.isSensorAvailable
import dev.ricknout.composesensors.rememberSensorValueAsState
import jano.net.futbolitopocket.ui.theme.FutbolitoPocketTheme

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

    val available = isSensorAvailable(type = Sensor.TYPE_ACCELEROMETER)

    val sensor = getSensor(type = Sensor.TYPE_ACCELEROMETER)

    val sensorValue by rememberSensorValueAsState(type = Sensor.TYPE_ACCELEROMETER) { event ->

    }

    var golesEquipoA:Int = 0
    var golesEquipoB:Int = 0

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
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = center
            )
        }
    }
}

