package com.example.smb1.Activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.example.smb1.R
import com.example.smb1.navigation.Shop
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddShopActivity : AppCompatActivity() {

    private var db: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc ->
                findViewById<EditText>(R.id.latitude).setText(loc.latitude.toString())
                findViewById<EditText>(R.id.longitude).setText(loc.longitude.toString())
            }
    }

    fun save(view: android.view.View) {
        val shop = Shop()
        shop.latitude = findViewById<EditText>(R.id.latitude).text.toString().toDouble()
        shop.longitude = findViewById<EditText>(R.id.longitude).text.toString().toDouble()
        shop.radius = findViewById<EditText>(R.id.shopRadius).text.toString().toDouble()
        shop.description = findViewById<EditText>(R.id.shopDescription).text.toString()
        shop.name = findViewById<EditText>(R.id.shopName).text.toString()

        val items = ArrayList<Shop>()
        val ref = db.getReference("users/" + mAuth.currentUser!!.uid)
            .child("shops")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(Shop::class.java)
                    items.add(item!!)
                }
                items.add(shop)
                db.getReference("users/" + mAuth.currentUser!!.uid)
                    .child("shops")
                    .setValue(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        finish()

    }
}