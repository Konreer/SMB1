package com.example.smb1.repository

import android.database.Cursor
import androidx.lifecycle.LiveData;
import com.example.smb1.dao.ShoppingListDao;
import com.example.smb1.entity.ShoppingList;
import com.example.smb1.relation.ShoppingListWithItems

class ShoppingListRepository(private val shoppingListDao: ShoppingListDao) {
    val allItems: LiveData<List<ShoppingListWithItems>> = shoppingListDao.getAllShoppingList()

    fun insert(shoppingList: ShoppingList){
        shoppingListDao.insert(shoppingList);
    }
    fun getShoppingListById(id: Long): ShoppingListWithItems {
        return shoppingListDao.getShoppingListById(id);
    }
    fun delete(shoppingList: ShoppingList){
        shoppingListDao.delete(shoppingList)
    }

    fun getAllShoppingListAsCursor(): Cursor{
        return shoppingListDao.getAllShoppingListAsCursor()
    }
}