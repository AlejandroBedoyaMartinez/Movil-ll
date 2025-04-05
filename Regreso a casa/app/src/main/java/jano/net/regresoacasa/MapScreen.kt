package jano.net.regresoacasa

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.utsman.osmandcompose.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

@Composable
fun MapScreen(navController: NavController) {
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
}


@SuppressLint("MissingPermission")
fun getCurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient, onLocationReceived: (android.location.Location) -> Unit) {
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        location?.let { onLocationReceived(it) }
    }
}