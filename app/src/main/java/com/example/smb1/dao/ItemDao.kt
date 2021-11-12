package com.example.smb1.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.smb1.entity.Item

@Dao
interface ItemDao {

    @Query(
        "SELECT * FROM item WHERE item.itemId = :id"
    )
    fun getItemById(id: Long): Item

    @Query(
        "SELECT * FROM item"
    )
    fun getAllItems(): LiveData<List<Item>>

    @Query(
        "SELECT * FROM item"
    )
    fun getAllItemsAsCursor(): Cursor

    @Query(
        "Select * FROM item WHERE item.shoppingListId = :id"
    )
    fun getItemsByListId(id: Long): LiveData<List<Item>>

    @Query(
        "SELECT * FROM item WHERE itemId = ( SELECT MAX(item.shoppingListId) FROM item)"
    )
    fun getItemsFromNewestList(): LiveData<List<Item>>

    @Insert(onConflict = REPLACE)
    fun insert(item: Item): Long

    @Update
    fun update(item: Item): Int

    @Delete
    fun delete(item: Item): Int

    @Query(
        "DELETE FROM item WHERE item.itemId = :id"
    )
    fun delete(id: Long): Int



}