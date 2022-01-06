package com.example.smb1.broadcast

import android.R
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.NotificationCompat

class ProximityReceiver : BroadcastReceiver() {
    private val PROX_ALERT_INTENT = "com.example.smb1.service.ProximityAlert"
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val key = LocationManager.KEY_PROXIMITY_ENTERING
        val entering = intent.getBooleanExtra(key, false)
        val mBuilder = NotificationCompat.Builder(context)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
        if (entering) {
            mBuilder
                .setContentTitle("Entering " + intent.getBundleExtra(PROX_ALERT_INTENT)!!.getString("shop"))
                .setContentText(intent.getBundleExtra(PROX_ALERT_INTENT)!!.getString("description"))
            Toast.makeText(context, "entering", Toast.LENGTH_SHORT).show()
        } else {
            mBuilder
                .setContentTitle("Leaving " + intent.getBundleExtra(PROX_ALERT_INTENT)!!.getString("shop"))
                .setContentText(intent.getBundleExtra(PROX_ALERT_INTENT)!!.getString("description"))
        }
        notificationManager.notify( intent.getBundleExtra(PROX_ALERT_INTENT)!!.getInt("id"), mBuilder.build());
    }
}