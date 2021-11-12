package com.example.smb1.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.smb1.database.DB
import com.example.smb1.entity.Item
import com.example.smb1.repository.ItemRepository

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository
    val allItems: LiveData<List<Item>>

    init {
        val itemDAO = DB.getDatabase(application).ItemDao()
        repository = ItemRepository(itemDAO)
        allItems = repository.allItems
    }

    fun getItemById(id: Long): Item{
        return repository.getItemById(id)
    }

    fun deleteItem(item: Item){
        repository.delete(item)
    }

    fun insertItem(item: Item){
        return repository.insert(item)
    }

    fun getItemsByListId(id: Long): LiveData<List<Item>> {
        return repository.getItemsByListId(id)
    }

    fun getItemsFromNewestList(): LiveData<List<Item>> {
        return repository.getItemsFromNewestList()
    }

    fun update(item: Item){
        repository.update(item)
    }

}