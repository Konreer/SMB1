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
import com.example.smb1.relation.ShoppingListWithItems
import com.example.smb1.viewmodel.ItemViewModel
import com.example.smb1.viewmodel.ShoppingListViewModel

class ShoppingListEditActivity : AppCompatActivity(), AdapterActionSetter<Item> {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var shoppingListWithItems: ShoppingListWithItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.shoppingListViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)
        this.itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        this.shoppingListWithItems = shoppingListViewModel.getShoppingListById(intent.extras!!.getLong("shoppingListId"))


        setContentView(R.layout.activity_shopping_list_edit)
        showShoppingList()
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

    fun showShoppingList() {

        val layout: RecyclerView = findViewById(R.id.recycler_view);
        val itemAdapter = ItemAdapter(this)

        itemViewModel.getItemsByListId(shoppingListWithItems.shoppingList.shoppingListId).observe(this, { items ->
            items?.let {
                itemAdapter.setItems(it)
            }
        })
        layout.adapter = itemAdapter
        layout.layoutManager = LinearLayoutManager(this)

    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, ItemEditActivity::class.java).apply {
            putExtra("itemId", item.itemId)
        }
        this.startActivity(intent)
    }

    fun deleteList(view: android.view.View) {
        shoppingListViewModel.delete(shoppingListWithItems.shoppingList)
    }

    fun addItem(view: android.view.View) {
        val intent = Intent(this, ItemEditActivity::class.java).apply {
            putExtra("shoppingListId", shoppingListWithItems.shoppingList.shoppingListId)
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