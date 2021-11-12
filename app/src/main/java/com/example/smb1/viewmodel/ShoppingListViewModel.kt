package com.example.smb1.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.smb1.database.DB
import com.example.smb1.entity.ShoppingList
import com.example.smb1.relation.ShoppingListWithItems
import com.example.smb1.repository.ShoppingListRepository

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShoppingListRepository
    val allItems: LiveData<List<ShoppingListWithItems>>

    init {
        val shoppingListDao = DB.getDatabase(application).ShoppingListDao()
        repository = ShoppingListRepository(shoppingListDao)
        allItems = repository.allItems
    }

    fun getShoppingListById(id: Long): ShoppingListWithItems {
        return repository.getShoppingListById(id)
    }

    fun insertShoppingList(shoppingList: ShoppingList){
        return repository.insert(shoppingList)
    }

    fun getAllShoppingLists(): LiveData<List<ShoppingListWithItems>> {
        return allItems
    }
    fun delete(shoppingList: ShoppingList){
        repository.delete(shoppingList)
    }

    fun getAllShoppingListAsCursor(): Cursor {
        return repository.getAllShoppingListAsCursor()
    }
}