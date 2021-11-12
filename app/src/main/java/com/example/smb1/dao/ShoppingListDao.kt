package com.example.smb1.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smb1.entity.ShoppingList
import com.example.smb1.relation.ShoppingListWithItems

@Dao
interface ShoppingListDao {
    @Transaction
    @Query(
        "SELECT * FROM shopping_list WHERE shopping_list.shoppingListId = :id"
    )
    fun getShoppingListById(id: Long): ShoppingListWithItems

    @Transaction
    @Query(
        "SELECT * FROM shopping_list"
    )
    fun getAllShoppingList(): LiveData<List<ShoppingListWithItems>>

    @Query(
        "SELECT * FROM shopping_list"
    )
    fun getAllShoppingListAsCursor(): Cursor

    @Insert
    fun insert(shoppingList: ShoppingList): Long

    @Update
    fun update(shoppingList: ShoppingList): Int

    @Delete
    fun delete(shoppingList: ShoppingList)

    @Query(
        "DELETE FROM shopping_list WHERE shoppingListId = :id"
    )
    fun delete(id: Long): Int
}