package com.example.smb1.service

import android.R
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.example.smb1.navigation.Shop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class ProximityNotifier : Service() {

    private var mBinder: MyBinder = MyBinder()
    private val PROX_ALERT_INTENT = "com.example.smb1.service.ProximityAlert"
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var requestCode = 0

    inner class MyBinder : Binder() {
        fun getService(): ProximityNotifier = this@ProximityNotifier
    }


    override fun onBind(intent: Intent): IBinder {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val ref = db.getReference("users/" + mAuth.currentUser!!.uid)
            .child("shops")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("MissingPermission")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in 0..requestCode) {
                    val pendingIntent = PendingIntent.getService(applicationContext, i, intent, 0)
                    locationManager.removeProximityAlert(pendingIntent)
                }
                requestCode = 0

                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(Shop::class.java)

                    val extras = Bundle()
                    extras.putString("shop", item!!.name)
                    extras.putString("description", item.description)
                    extras.putInt("id", requestCode);
                    val intent = Intent(PROX_ALERT_INTENT)
                    intent.putExtra(PROX_ALERT_INTENT, extras)
                    val proximityIntent = PendingIntent.getBroadcast(
                        applicationContext,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )

                    locationManager.addProximityAlert(
                        item.latitude, item.longitude, item.radius.toFloat(), -1, proximityIntent
                    )

                    requestCode++
                }


            }
            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

        return mBinder;

    }
}