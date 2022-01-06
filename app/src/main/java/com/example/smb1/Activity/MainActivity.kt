package com.example.smb1.Activity

import android.content.*
import android.graphics.Typeface
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.ItemAdapter
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.ItemFirebase
import com.example.smb1.entity.ShoppingListFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.example.smb1.broadcast.ProximityReceiver
import com.example.smb1.service.ProximityNotifier


class MainActivity : AppCompatActivity(), AdapterActionSetter<ItemFirebase> {

    private val PROX_ALERT_INTENT = "com.example.smb1.service.ProximityAlert"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mserv: ProximityNotifier
    private var mBound: Boolean = false
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private val mcom = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ProximityNotifier.MyBinder
            mserv = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, ProximityNotifier::class.java)
        bindService(intent, mcom, Context.BIND_AUTO_CREATE)
        val filter = IntentFilter(PROX_ALERT_INTENT)
        registerReceiver(ProximityReceiver(), filter)
        setContentView(R.layout.activity_main)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        else {
            setContentView(R.layout.activity_main)
            showCurrentShoppingList()
        }


    }

    fun showCurrentShoppingList() {

        val layout: RecyclerView = findViewById(R.id.recycler_view);
        val itemAdapter = ItemAdapter(this)

        db.getReference("users/" + mAuth.currentUser!!.uid + "/lists")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var list = ArrayList<ShoppingListFirebase>()
                    for (messageSnapshot in dataSnapshot.children) {
                        list.add(messageSnapshot.getValue(ShoppingListFirebase::class.java) as ShoppingListFirebase)
                    }
                    if (list.size > 0) {
                        var mostRecentShoppingList: ShoppingListFirebase = list[0]
                        for (shoppingList in list) {
                            if (mostRecentShoppingList.date < shoppingList.date) {
                                mostRecentShoppingList = shoppingList
                            }
                        }
                        itemAdapter.setItems(mostRecentShoppingList.items, mostRecentShoppingList.date.toString())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("readDB-error", databaseError.details)
                }
            })

        layout.adapter = itemAdapter
        layout.layoutManager = LinearLayoutManager(this)

    }

    fun goToShoppingLists(view: android.view.View) {
        startActivity(Intent(this, ShoppingListActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        this.startActivity(intent)
        return true
    }

    override fun onResume() {
        updateFontAndFontFamily()
        showCurrentShoppingList()
        super.onResume()
    }

    fun updateFontAndFontFamily() {
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f) {
            findViewById<Button>(R.id.button).textSize = fontSize
            findViewById<TextView>(R.id.textView).textSize = fontSize
        }

        if (!fontFamily.equals("")) {
            findViewById<Button>(R.id.button).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<TextView>(R.id.textView).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
        }
    }

    override fun onItemClick(item: ItemFirebase, listName: String, index: Int) {
        val intent = Intent(this, ItemEditActivity::class.java).apply {
            putExtra("path", "users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items/" + index)
            putExtra("index", index)
            putExtra("listName", listName)
        }
        this.startActivity(intent)
    }

    override fun onCheckboxClick(item: ItemFirebase, isChecked: Boolean, listName: String) {
        item.checked = isChecked

        val ref = db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName).child("items")
        val items = ArrayList<ItemFirebase>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(ItemFirebase::class.java)
                    items.add(item!!)
                }
                items[items.indexOf(item)].checked = isChecked
                db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items").setValue(items)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

    }

    override fun setFont(list: List<TextView>) {
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        for (textView in list) {
            if (fontSize != 0f) {
                textView.textSize = fontSize
            }

            if (!fontFamily.equals("")) {
                textView.typeface = Typeface.createFromAsset(this.assets, fontFamily)
            }
        }
    }

    fun goToShopsList(view: android.view.View) {
        startActivity(Intent(this, ShopListActivity::class.java))
    }
    fun goToMap(view: android.view.View) {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    override fun onStop() {
        super.onStop()
        if(mBound){
            unbindService(mcom)
            mBound = false
        }
    }
}