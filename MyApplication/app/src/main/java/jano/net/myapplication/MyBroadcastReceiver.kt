package jano.net.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val estado = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val numero = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (estado == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("llamada receiver", "Llamada entrante de: $numero")
            }
        }
    }
}