package com.example.smb1.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.ItemAdapter
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.Item
import com.example.smb1.entity.ItemFirebase
import com.example.smb1.relation.ShoppingListWithItems
import com.example.smb1.viewmodel.ItemViewModel
import com.example.smb1.viewmodel.ShoppingListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShoppingListEditActivity : AppCompatActivity(), AdapterActionSetter<ItemFirebase> {

    private var db: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shopping_list_edit)
        showShoppingList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        this.startActivity(intent)
        return true
    }

    fun showShoppingList() {

        val layout: RecyclerView = findViewById(R.id.recycler_view);
        val itemAdapter = ItemAdapter(this)

        val listName = intent.extras?.getString("listName")

        val ref = db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName).child("items")
        val items = ArrayList<ItemFirebase>()

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(ItemFirebase::class.java)
                    items.add(item!!)
                }
                itemAdapter.setItems(items, listName!!)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

        layout.adapter = itemAdapter
        layout.layoutManager = LinearLayoutManager(this)

    }

    override fun onItemClick(item: ItemFirebase, listName: String, index: Int) {
        val intent = Intent(this, ItemEditActivity::class.java).apply {
            putExtra("path", "users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items/" + index)
            putExtra("index", index)
            putExtra("listName", listName)
        }
        this.startActivity(intent)
    }

    fun deleteList(view: android.view.View) {
        val listName = intent.extras?.getString("listName")
        db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName).removeValue()
        this.finish()
    }

    fun addItem(view: android.view.View) {
        val listName = intent.extras?.getString("listName")
        val intent = Intent(this, ItemEditActivity::class.java).apply {
            putExtra("path", "users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items")
            putExtra("index", -1)
            putExtra("listName", listName)
        }
        this.startActivity(intent)
    }

    override fun onResume() {
        updateFontAndFontFamily()
        showShoppingList()
        super.onResume()
    }

    fun updateFontAndFontFamily(){
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f){
            findViewById<Button>(R.id.deletList).textSize = fontSize
            findViewById<Button>(R.id.addItem).textSize = fontSize
        }

        if (!fontFamily.equals("")){
            findViewById<Button>(R.id.deletList).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<Button>(R.id.addItem).typeface = Typeface.createFromAsset(this.assets, fontFamily)
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