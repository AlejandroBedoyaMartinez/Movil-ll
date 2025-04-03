package jano.net.regresoacasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.utsman.osmandcompose.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen()
        }
    }
}

@Composable
fun MapScreen() {
    val overlayManagerState = rememberOverlayManagerState()

    // Configurar propiedades del mapa
    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties.copy(
            isTilesScaledToDpi = true,
            tileSources = TileSourceFactory.MAPNIK,  // Fuente de OpenStreetMap
            isEnableRotationGesture = true,
            zoomButtonVisibility = ZoomButtonVisibility.ALWAYS,
            isMultiTouchControls = true
        ))
    }

    // Estado de la cámara (ubicación inicial y zoom)
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(19.432608, -99.133209)  // CDMX
        zoom = 15.0
    }

    // Definir una ruta con Polyline
    val geoPointPolyline = remember {
        listOf(
            GeoPoint(19.432608, -99.133209),
            GeoPoint(19.435, -99.14),
            GeoPoint(19.437, -99.145)
        )
    }

    // Estado del marcador
    val markerState = rememberMarkerState(
        geoPoint = GeoPoint(19.432608, -99.133209)
    )

    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        properties = mapProperties,
        overlayManagerState = overlayManagerState
    ) {
        // Agregar marcador en la ubicación inicial
        Marker(
            state = markerState
        )

        // Dibujar la ruta
        Polyline(
            geoPoints = geoPointPolyline
        )
    }
}
