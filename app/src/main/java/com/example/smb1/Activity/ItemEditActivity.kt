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
import com.example.smb1.viewmodel.ItemViewModel


class ItemEditActivity() : AppCompatActivity() {
    private val ACTION_ITEM_ADDED = "com.example.smb1.Activity.ItemEditActivity.ITEMADDED"

    private lateinit var item: Item
    private lateinit var itemViewModel: ItemViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_edit)
        this.itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        var itemId = intent.extras?.getLong("itemId")

        if (itemId!!.toInt() != 0 ) {
            this.item = itemViewModel.getItemById(intent.extras!!.getLong("itemId"))
        }
        else {
            var deleteButton: Button = this.findViewById(R.id.deleteButton)
            deleteButton.visibility = View.INVISIBLE
            var shoppingListId = intent.extras!!.getLong("shoppingListId")
            this.item = Item("name", 0.0, 0, false, shoppingListId)
        }




        findViewById<EditText>(R.id.editTextPrice).setText(item.price.toString())
        findViewById<EditText>(R.id.editTextAmount).setText(item.amount.toString())
        findViewById<EditText>(R.id.editTextTextPersonName).setText(item.name)

        findViewById<EditText>(R.id.editTextPrice).filters = arrayOf(DecimalDigitsInputFilter(2))
        updateFontAndFontFamily()
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

    fun updateFontAndFontFamily(){
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f){
            findViewById<EditText>(R.id.editTextAmount).textSize = fontSize
            findViewById<EditText>(R.id.editTextTextPersonName).textSize = fontSize
            findViewById<EditText>(R.id.editTextPrice).textSize = fontSize
            findViewById<Button>(R.id.saveButton).textSize = fontSize
            findViewById<Button>(R.id.deleteButton).textSize = fontSize
        }

        if (!fontFamily.equals("")){
            findViewById<EditText>(R.id.editTextAmount).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<EditText>(R.id.editTextTextPersonName).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<EditText>(R.id.editTextPrice).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<Button>(R.id.saveButton).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<Button>(R.id.deleteButton).typeface = Typeface.createFromAsset(this.assets, fontFamily)
        }
    }

    fun saveEdit(view: android.view.View) {
        item.name=findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
        item.amount=findViewById<EditText>(R.id.editTextAmount).text.toString().toInt()
        item.price=findViewById<EditText>(R.id.editTextPrice).text.toString().toDouble()
        val id = itemViewModel.insertItem(item)
        finish()

        var itemId = intent.extras?.getLong("itemId")

        if (itemId!!.toInt() == 0 ) {
            var sendIntent = Intent(ACTION_ITEM_ADDED)
            sendIntent.setAction(ACTION_ITEM_ADDED)
            sendIntent.putExtra("class", componentName.className)
            sendIntent.putExtra("package", this.packageName)
            sendIntent.putExtra("itemId", id)
            sendIntent.putExtra("amount", item.amount)
            sendIntent.putExtra("name", item.name)
            sendIntent.putExtra("price", item.price)
            sendBroadcast(sendIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES), "com.example.myapp.PERMISSION")

        }
    }

    override fun onPause() {
        finish()
        super.onPause()
    }

    fun deleteItem(view: android.view.View) {
        itemViewModel.deleteItem(item)
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