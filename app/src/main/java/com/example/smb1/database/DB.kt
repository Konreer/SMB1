package com.example.smb1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smb1.dao.ItemDao
import com.example.smb1.dao.ShoppingListDao
import com.example.smb1.entity.Item
import com.example.smb1.entity.ShoppingList
import com.example.smb1.converter.Converters

@Database(entities = [Item::class, ShoppingList::class], version = 1)
@TypeConverters(Converters::class)
abstract class DB : RoomDatabase() {

    abstract fun ItemDao(): ItemDao
    abstract fun ShoppingListDao(): ShoppingListDao

    //singleton
    companion object{
        private var instance: DB? = null

        fun getDatabase(context: Context): DB{
            val tmpInst = instance
            if(tmpInst != null){
                return tmpInst
            }
            val inst = Room.databaseBuilder(
                context.applicationContext,
                DB::class.java,
                "shoppingListDB"
            ).allowMainThreadQueries().build()
            instance = inst
            return inst
        }
    }

}