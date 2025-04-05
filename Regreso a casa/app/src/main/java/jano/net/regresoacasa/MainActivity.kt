package jano.net.regresoacasa

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.rememberOverlayManagerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            principal()
        }
    }
}

@Composable
fun principal() {
    var direccion by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Ingresa tu dirección",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            modifier = Modifier
                .width(200.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            onClick = {
                show = !show
            })
        {
            Text(text = "Buscar")
        }
    }

    if (show) {
        MapScreen(setShow = { show = it })
    }
}

@Composable
fun MapScreen(setShow: (Boolean) -> Unit) {
    BackHandler {
        setShow(false)
    }

    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val overlayManagerState = rememberOverlayManagerState()
    var currentLocation by remember { mutableStateOf(GeoPoint(19.432608, -99.133209)) }
    var isFirstLocationSet by remember { mutableStateOf(false) }

    val mapProperties = remember {
        DefaultMapProperties.copy(
            isTilesScaledToDpi = true,
            tileSources = TileSourceFactory.MAPNIK,
            isEnableRotationGesture = true,
            zoomButtonVisibility = ZoomButtonVisibility.ALWAYS,
            isMultiTouchControls = true
        )
    }

    val cameraState = rememberCameraState {
        geoPoint
        zoom
    }
    val markerState = rememberMarkerState()

    LaunchedEffect(Unit) {
        getCurrentLocation(fusedLocationProviderClient) { location ->
            if (!isFirstLocationSet) {
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                currentLocation = geoPoint
                cameraState.geoPoint = geoPoint
                cameraState.zoom = 15.0
                markerState.geoPoint = geoPoint
                isFirstLocationSet = true
            }
        }
    }

    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        properties = mapProperties,
        overlayManagerState = overlayManagerState
    ) {
        Marker(state = markerState)
    }

    IconButton(
        onClick = { setShow(false) },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_revert),
            contentDescription = "Volver",
            tint = Color.Black
        )
    }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient, onLocationReceived: (android.location.Location) -> Unit) {
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        location?.let { onLocationReceived(it) }
    }
}
