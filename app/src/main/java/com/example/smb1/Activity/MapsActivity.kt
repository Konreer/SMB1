package com.example.smb1.Activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.ColorUtils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.smb1.R
import com.example.smb1.databinding.ActivityMapsBinding
import com.example.smb1.navigation.Shop
import com.google.android.gms.location.places.internal.PlaceEntity
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.collection.LLRBNode
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var db: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var API_KEY: String
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fillMap()
    }


    fun fillMap(){
        val items: ArrayList<Shop> = ArrayList()
        val ref = db.getReference("users/" + mAuth.currentUser!!.uid)
            .child("shops")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(Shop::class.java)
                    if (item != null) {
                        val position = LatLng(item.latitude, item.longitude)
                        mMap.addMarker(MarkerOptions().position(position).title(item.name).snippet(item.description))
                        mMap.addCircle(CircleOptions().center(position).radius(item.radius).fillColor(Color.HSVToColor(20, floatArrayOf(174f, 59f, 50f))).strokeColor(Color.HSVToColor(floatArrayOf(174f, 70f, 50f))))
                    }
                    items.add(item!!)
                }
                db.getReference("users/" + mAuth.currentUser!!.uid)
                    .child("shops")
                    .setValue(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }
}