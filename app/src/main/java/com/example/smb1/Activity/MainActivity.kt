package com.example.smb1.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.ItemAdapter
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.Item
import com.example.smb1.viewmodel.ItemViewModel


class MainActivity : AppCompatActivity(), AdapterActionSetter<Item> {

    private lateinit var itemViewModel: ItemViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        setContentView(R.layout.activity_main)
        showCurrentShoppingList()
    }

    fun showCurrentShoppingList(){

        val layout : RecyclerView = findViewById(R.id.recycler_view);
        val itemAdapter = ItemAdapter(this)

        itemViewModel.getItemsFromNewestList().observe(this, { items ->
            items?.let {
                itemAdapter.setItems(it)
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

    override fun onResume()
    {
        updateFontAndFontFamily()
        showCurrentShoppingList()
        super.onResume()
    }

    fun updateFontAndFontFamily(){
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f){
            findViewById<Button>(R.id.button).textSize = fontSize
            findViewById<TextView>(R.id.textView).textSize = fontSize
        }

        if (!fontFamily.equals("")){
            findViewById<Button>(R.id.button).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<TextView>(R.id.textView).typeface = Typeface.createFromAsset(this.assets, fontFamily)
        }
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, ItemEditActivity::class.java).apply {
            putExtra("itemId", item.itemId)
        }
        this.startActivity(intent)
    }

    override fun onCheckboxClick(item: Item, isChecked: Boolean) {
        item.checked = isChecked
        itemViewModel.update(item)
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