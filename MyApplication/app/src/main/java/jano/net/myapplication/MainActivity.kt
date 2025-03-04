package jano.net.myapplication


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jano.net.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                UI(this)
            }
        }
    }
}

@Composable
fun UI(context: Context) {
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }  // Variable para mostrar el diálogo
    val sharedPreferences = context.getSharedPreferences("LlamadaReceiver", Context.MODE_PRIVATE)

    // Iniciar servicio
    val serviceIntent = Intent(context, ServicePhoneState::class.java)
    context.startService(serviceIntent)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Número a detectar:")
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text("Ej: 6505551212") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Mensaje a enviar:")
        TextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text("Escribe el mensaje aquí") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                saveUserPreferences(sharedPreferences, phoneNumber, message)
                showDialog = true  // Mostrar el cuadro de diálogo al guardar
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar número y mensaje")
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("¡Éxito!") },
            text = { Text("Número y mensaje guardados correctamente.") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

fun saveUserPreferences(sharedPreferences: SharedPreferences, phone: String, message: String) {
    sharedPreferences.edit().apply {
        putString("numero", phone)
        putString("mensaje", message)
        apply()
    }
}
