package com.example.smb1.repository

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.example.smb1.dao.ItemDao
import com.example.smb1.entity.Item

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: LiveData<List<Item>> = itemDao.getAllItems()

    fun insert(item: Item){
        itemDao.insert(item);
    }
    fun getItemById(id: Long): Item {
        return itemDao.getItemById(id);
    }

    fun getItemsByListId(id: Long): LiveData<List<Item>> {
        return itemDao.getItemsByListId(id)
    }

    fun getItemsFromNewestList(): LiveData<List<Item>> {
        return itemDao.getItemsFromNewestList()
    }


    fun getItemsAsCursor(): Cursor {
        return itemDao.getAllItemsAsCursor()
    }

    fun delete(item: Item){
        itemDao.delete(item)
    }

    fun update(item: Item){
        itemDao.update(item)
    }
}