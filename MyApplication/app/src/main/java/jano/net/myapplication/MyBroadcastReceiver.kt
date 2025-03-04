package jano.net.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val estado = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val numeroEntrante = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (estado == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("LlamadaReceiver", "Llamada entrante de: $numeroEntrante")

                val sharedPreferences = context?.getSharedPreferences("LlamadaReceiver", Context.MODE_PRIVATE)
                val numeroGuardado = sharedPreferences?.getString("numero", "")
                val mensaje = sharedPreferences?.getString("mensaje", "")
                Log.d("LlamadaReceiver", numeroGuardado + mensaje + "")

                if (numeroEntrante == numeroGuardado) {
                    Log.d("LlamadaReceiver", "¡Número detectado! Enviando SMS...")
                    enviarSMS(numeroEntrante, mensaje ?: "", context)
                }
            }
        }
    }

    private fun enviarSMS(numero: String?, mensaje: String, context: Context?) {
        if (numero != null && mensaje.isNotEmpty() && context != null) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(numero, null, mensaje, null, null)
                Log.d("LlamadaReceiver", "SMS enviado correctamente a $numero")
            } catch (e: Exception) {
                Log.e("LlamadaReceiver", "Error al enviar SMS: ${e.message}")
            }
        }
    }
}
