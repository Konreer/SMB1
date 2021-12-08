package com.example.smb1.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
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
import com.example.smb1.adapter.ShoppingListAdapter
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.ShoppingListFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ShoppingListActivity : AppCompatActivity(), AdapterActionSetter<ShoppingListFirebase> {

    private var db: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shopping_list)
        showShoppingLists()
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

    fun showShoppingLists(){

        val layout : RecyclerView = findViewById(R.id.recycler_view);
        val itemAdapter = ShoppingListAdapter(this)

        db.getReference("users/" + mAuth.currentUser!!.uid + "/lists")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var list = ArrayList<ShoppingListFirebase>()
                    for (messageSnapshot in dataSnapshot.children) {
                        list.add(messageSnapshot.getValue(ShoppingListFirebase::class.java) as ShoppingListFirebase)
                    }
                    itemAdapter.setLists(list)

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
    fun addList(view: android.view.View) {
        val ref = db.getReference("users/"+ mAuth.currentUser!!.uid).child("lists")
        val sl = ShoppingListFirebase(Date())
        ref.child(sl.date.toString()).setValue(sl)
    }

    override fun onItemClick(item: ShoppingListFirebase, listName: String, index: Int) {
        val intent = Intent(this, ShoppingListEditActivity::class.java).apply {
            putExtra("path", "users/" + mAuth.currentUser!!.uid + "/lists/" + listName)
            putExtra("index", index)
            putExtra("listName", listName)
        }
        this.startActivity(intent)
    }

    override fun onResume() {
        updateFontAndFontFamily()
        showShoppingLists()
        super.onResume()
    }

    fun updateFontAndFontFamily(){
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f){
            findViewById<Button>(R.id.addList).textSize = fontSize
        }

        if (!fontFamily.equals("")){
            findViewById<Button>(R.id.addList).typeface = Typeface.createFromAsset(this.assets, fontFamily)
        }
    }

    override fun setFont(list: List<TextView>) {
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        for (textView in list) {
            if (fontSize != 0f){
                textView.textSize = fontSize
            }

            if (!fontFamily.equals("")){
                textView.typeface = Typeface.createFromAsset(this.assets, fontFamily)
            }
        }
    }

}