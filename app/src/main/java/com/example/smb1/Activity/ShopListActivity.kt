package com.example.smb1.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.ShopListAdapter
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.ItemFirebase
import com.example.smb1.navigation.Shop
import com.example.smb1.entity.ShoppingListFirebase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ShopListActivity : AppCompatActivity(), AdapterActionSetter<Shop> {

    private lateinit var API_KEY: String
    private lateinit var location: LatLng
    private var db: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        API_KEY = getString(R.string.google_maps_key)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val layout: RecyclerView = findViewById(R.id.recycler_view);
        val shopAdapter = ShopListAdapter(this)

        db.getReference("users/" + mAuth.currentUser!!.uid + "/shops")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var list = ArrayList<Shop>()
                    for (messageSnapshot in dataSnapshot.children) {
                        list.add(messageSnapshot.getValue(Shop::class.java) as Shop)
                    }
                    shopAdapter.setItems(list, "shops")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("readDB-error", databaseError.details)
                }
            })

        layout.adapter = shopAdapter
        layout.layoutManager = LinearLayoutManager(this)
        getLastKnownLocation()

    }

    fun addNearestShop(view: android.view.View) {
        startActivity(Intent(this, AddShopActivity::class.java))
    }

    override fun onItemClick(item: Shop, listName: String, index: Int) {
        TODO("Not yet implemented")
    }

    override fun setFont(list: List<TextView>) {
        TODO("Not yet implemented")
    }

    override fun onCheckboxClick(shop: Shop, isChecked: Boolean, listName: String) {
        val ref = db.getReference("users/" + mAuth.currentUser!!.uid)
            .child("shops")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = ArrayList<Shop>()
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(Shop::class.java)
                    items.add(item!!)
                }
                items.remove(shop)
                db.getReference("users/" + mAuth.currentUser!!.uid)
                    .child("shops")
                    .setValue(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })


    }

    fun getLastKnownLocation() {
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
                location = LatLng(loc.latitude, loc.longitude)
//                var request = Request.Builder().url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${loc.latitude},${loc.longitude}&radius=1000&keyword=sklep&key=${API_KEY}")
//                    .build()
//
//                var response = OkHttpClient().newCall(request).execute().body
//                var jsonObject = JSONObject(response?.string()) // This will make the json below as an object for you
//
//                val results = jsonObject.getJSONArray("results")
//                val ref = db.getReference("users/" + mAuth.currentUser!!.uid)
//                val shops = ArrayList<Shop>()
//
//                var url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${location.latitude},${location.longitude}&destinations="
//
//                for (i in 0 until results.length()) {
//                    val coordinates =
//                        JSONObject(results.get(i).toString()).getJSONObject("geometry")
//                            .getJSONObject("location");
//                    val shop = Shop(
//                        JSONObject(results.get(i).toString()).getString("name"),
//                        JSONObject(results.get(i).toString()).getString("types"),
//                        10,
//                        true,
//                        LatLng(coordinates.getDouble("lat"), coordinates.getDouble("lng"))
//                    )
//                    shops.add(shop)
//                    url+="${shop.location.latitude},${shop.location.longitude}"
//                    if (i!=results.length()-1) url+="|"
//                }
//
//                url+="&mode=walking&key=${API_KEY}"
//
//                request = Request.Builder().url(url)
//                    .build()
//
//                response = OkHttpClient().newCall(request).execute().body
//                jsonObject = JSONObject(response?.string())


//
//                    ref.child("shops").addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            for (itemSnapshot in dataSnapshot.children) {
//                                val item = itemSnapshot.getValue(Shop::class.java)
//                                shops.add(item!!)
//                            }
//                            db.getReference("users/" + mAuth.currentUser!!.uid + "/shops")
//                                .setValue(shops)
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//                            throw databaseError.toException()
//                        }
//                    })





            }

    }
}