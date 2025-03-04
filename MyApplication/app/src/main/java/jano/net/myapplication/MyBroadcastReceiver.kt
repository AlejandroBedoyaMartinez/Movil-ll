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
            val numero = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (estado == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("llamada receiver", "Llamada entrante de: $numero")

                if (numero == "6505551212") {
                    Log.d("llamada receiver", "¡Es el número deseado!")
                    sendSMS(context, numero, "Estoy ocupado, te llamaré más tarde.")
                }
            }
        }
    }

    private fun sendSMS(context: Context?, phoneNumber: String?, message: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        if (phoneNumber != null) {
            try {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d("llamada receiver", "Mensaje enviado a: $phoneNumber")
            } catch (e: Exception) {
                Log.e("llamada receiver", "Error al enviar el mensaje: ${e.message}")
            }
        }
    }
}