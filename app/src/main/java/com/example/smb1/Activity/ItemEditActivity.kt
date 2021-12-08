package com.example.smb1.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.smb1.R
import com.example.smb1.entity.Item
import com.example.smb1.entity.ItemFirebase
import com.example.smb1.viewmodel.ItemViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ItemEditActivity() : AppCompatActivity() {
    private val ACTION_ITEM_ADDED = "com.example.smb1.Activity.ItemEditActivity.ITEMADDED"

    private lateinit var item: ItemFirebase
    private lateinit var path: String
    private var db: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://smb3-7fa67-default-rtdb.europe-west1.firebasedatabase.app")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var path = intent.extras?.getString("path")
        val index = intent.extras?.getInt("index")

        if (index!! != -1) {
            this.path = path!!
            db.getReference(path).get().addOnCompleteListener(this) {
                this.item = it.result!!.getValue(ItemFirebase::class.java)!!
                setContentView(R.layout.activity_item_edit)
                findViewById<EditText>(R.id.editTextPrice).setText(item.price.toString())
                findViewById<EditText>(R.id.editTextAmount).setText(item.amount.toString())
                findViewById<EditText>(R.id.editTextTextPersonName).setText(item.name)

                findViewById<EditText>(R.id.editTextPrice).filters =
                    arrayOf(DecimalDigitsInputFilter(2))
                updateFontAndFontFamily()
            }
        } else {
            setContentView(R.layout.activity_item_edit)
            val deleteButton: Button = this.findViewById(R.id.deleteButton)
            deleteButton.visibility = View.INVISIBLE
            this.item = ItemFirebase("name", 0.0, 0, false)
            findViewById<EditText>(R.id.editTextPrice).setText(item.price.toString())
            findViewById<EditText>(R.id.editTextAmount).setText(item.amount.toString())
            findViewById<EditText>(R.id.editTextTextPersonName).setText(item.name)

            findViewById<EditText>(R.id.editTextPrice).filters =
                arrayOf(DecimalDigitsInputFilter(2))
            updateFontAndFontFamily()
        }


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

    fun updateFontAndFontFamily() {
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f) {
            findViewById<EditText>(R.id.editTextAmount).textSize = fontSize
            findViewById<EditText>(R.id.editTextTextPersonName).textSize = fontSize
            findViewById<EditText>(R.id.editTextPrice).textSize = fontSize
            findViewById<Button>(R.id.saveButton).textSize = fontSize
            findViewById<Button>(R.id.deleteButton).textSize = fontSize
        }

        if (!fontFamily.equals("")) {
            findViewById<EditText>(R.id.editTextAmount).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<EditText>(R.id.editTextTextPersonName).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<EditText>(R.id.editTextPrice).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<Button>(R.id.saveButton).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<Button>(R.id.deleteButton).typeface =
                Typeface.createFromAsset(this.assets, fontFamily)
        }
    }

    fun saveEdit(view: android.view.View) {

        //val id = itemViewModel.insertItem(item)

        val index = intent.extras?.getInt("index")
        val listName = intent.extras?.getString("listName")
        if (index == -1) {
            val item = this.item
            val items = ArrayList<ItemFirebase>()
            val ref = db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName)
                .child("items")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (itemSnapshot in dataSnapshot.children) {
                        val item = itemSnapshot.getValue(ItemFirebase::class.java)
                        items.add(item!!)
                    }
                    item.name = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
                    item.amount =
                        findViewById<EditText>(R.id.editTextAmount).text.toString().toInt()
                    item.price =
                        findViewById<EditText>(R.id.editTextPrice).text.toString().toDouble()
                    items.add(item)
                    db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items")
                        .setValue(items)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        } else {

            val item = this.item
            val items = ArrayList<ItemFirebase>()
            val ref = db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName)
                .child("items")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (itemSnapshot in dataSnapshot.children) {
                        val itemFromSnapshot = itemSnapshot.getValue(ItemFirebase::class.java)
                        items.add(itemFromSnapshot!!)
                    }
                    val indexOfItemToChange = items.indexOf(item)
                    item.name = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
                    item.amount =
                        findViewById<EditText>(R.id.editTextAmount).text.toString().toInt()
                    item.price =
                        findViewById<EditText>(R.id.editTextPrice).text.toString().toDouble()
                    items[indexOfItemToChange] = item
                    db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items")
                        .setValue(items)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        }
        finish()
        if (index == -1) {
            val sendIntent = Intent(ACTION_ITEM_ADDED)
            sendIntent.setAction(ACTION_ITEM_ADDED)
            sendIntent.putExtra("class", componentName.className)
            sendIntent.putExtra("package", this.packageName)
            sendIntent.putExtra("amount", item.amount)
            sendIntent.putExtra("name", item.name)
            sendIntent.putExtra("price", item.price)
            sendBroadcast(
                sendIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES),
                "com.example.myapp.PERMISSION"
            )

        }
    }

    override fun onPause() {
        finish()
        super.onPause()
    }

    fun deleteItem(view: android.view.View) {
        val listName = intent.extras?.getString("listName")
        val item = this.item
        val items = ArrayList<ItemFirebase>()
        val ref = db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName)
            .child("items")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val itemFromSnapshot = itemSnapshot.getValue(ItemFirebase::class.java)
                    items.add(itemFromSnapshot!!)
                }
                items.remove(item)
                db.getReference("users/" + mAuth.currentUser!!.uid + "/lists/" + listName + "/items")
                    .setValue(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        finish()
    }
}


class DecimalDigitsInputFilter(private val decimalDigits: Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        var dotPos = -1
        val len = dest.length
        for (i in 0 until len) {
            val c = dest[i]
            if (c == '.' || c == ',') {
                dotPos = i
                break
            }
        }
        if (dotPos >= 0) {

            // protects against many dots
            if (source == "." || source == ",") {
                return ""
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null
            }
            if (len - dotPos > decimalDigits) {
                return ""
            }
        }
        return null
    }
}