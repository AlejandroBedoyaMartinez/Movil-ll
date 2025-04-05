package jano.net.regresoacasa

import android.content.Context
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
import com.utsman.osmandcompose.Polyline

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            principal(this)
        }
    }
}

@Composable
fun principal(context: Context) {
    var direccion by remember { mutableStateOf("Calle P. Javier Cardoso 12, Santiago Maravatío, Guanajuato") }
    var show by remember { mutableStateOf(false) }
    var routeCoordinates by remember { mutableStateOf<List<List<Double>>>(emptyList()) }
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
        Text(text = " Ejemplo: (Calle numero), (ciudad) , (estado)",
            modifier = Modifier.padding(16.dp),
        )
        Button(
            modifier = Modifier
                .width(200.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            onClick = {
                show = true
                if (direccion.isNotEmpty()) {
                    getCurrentLocation(
                        LocationServices.getFusedLocationProviderClient(context)
                    ) { location ->
                        getLocationAndRoute(
                            location.latitude,
                            location.longitude,
                            direccion
                        ) { coordinates ->
                            routeCoordinates = coordinates
                        }
                    }
                }
            }
        ) {
            Text(text = "Buscar")
        }
    }
    if (show) {
        MapScreen(setShow = { show = it }, routeCoordinates = routeCoordinates)
    }
}

@Composable
fun MapScreen(setShow: (Boolean) -> Unit, routeCoordinates: List<List<Double>>) {
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
        if (routeCoordinates.isNotEmpty()) {
            val geoPoints = routeCoordinates.mapNotNull { coord ->
                if (coord.size >= 2) GeoPoint(coord[1], coord[0]) else null
            }
            Polyline(geoPoints = geoPoints)
        }
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