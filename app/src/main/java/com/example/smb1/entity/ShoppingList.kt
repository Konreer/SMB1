package com.example.smb1.entity

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shopping_list",)
data class ShoppingList(var date: Date){
    @PrimaryKey(autoGenerate = true)
    var shoppingListId: Long = 0

    companion object {
        fun fromContentValues(contentValues: ContentValues): ShoppingList? {
            var date: Date = Date()

            if (contentValues.containsKey("date")) {
                date = Date(contentValues.getAsLong("date"))
            }
            return ShoppingList(date)
        }
    }

}