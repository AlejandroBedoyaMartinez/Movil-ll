package jano.net.regresoacasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController()
        }
    }
}

@Composable
fun navController() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "principal") {
        composable("principal") { principal(navController) }
        composable("map") { MapScreen(navController) }
    }
}

@Composable
fun principal(navController: NavController) {
    var direccion by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Ingresa tu dirección",
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Button(
            onClick =
            {
                if(direccion != ""){
                navController.navigate("map")
                }
            })
        {
            Text(text = "Buscar")
        }
    }
}

