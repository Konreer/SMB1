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
import com.example.smb1.adapter.ShoppingListAdapter
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.ShoppingList
import com.example.smb1.relation.ShoppingListWithItems
import com.example.smb1.viewmodel.ShoppingListViewModel
import java.util.*

class ShoppingListActivity : AppCompatActivity(), AdapterActionSetter<ShoppingListWithItems> {
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.shoppingListViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)

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

        shoppingListViewModel.getAllShoppingLists().observe(this, { items ->
            items?.let {
                itemAdapter.setLists(it)
            }
        })
        layout.adapter = itemAdapter
        layout.layoutManager = LinearLayoutManager(this)

    }

    fun goToShoppingLists(view: android.view.View) {
        startActivity(Intent(this, ShoppingListActivity::class.java))
    }
    fun addList(view: android.view.View) {
        shoppingListViewModel.insertShoppingList(ShoppingList(Date()))
    }

    override fun onItemClick(item: ShoppingListWithItems) {
        val intent = Intent(this, ShoppingListEditActivity::class.java).apply {
            putExtra("shoppingListId", item.shoppingList.shoppingListId)
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