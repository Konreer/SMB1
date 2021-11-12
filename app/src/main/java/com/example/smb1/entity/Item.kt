package com.example.smb1.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import android.content.ContentValues
import android.text.BoringLayout


@Entity(
    tableName = "item",

)
data class Item(var name: String, var price: Double, var amount: Int, var checked: Boolean, @ColumnInfo(index = true) var shoppingListId: Long) {
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0

     companion object {
         fun fromContentValues(contentValues: ContentValues): Item? {
             var name: String = ""
             var price: Double = 0.0
             var amount: Int = 0
             var checked: Boolean = false
             var shoppingListId: Long = -1

             if (contentValues.containsKey("name")) {
                 name = contentValues.getAsString("name")
             }
             if (contentValues.containsKey("price")) {
                 price = contentValues.getAsDouble("price")
             }
             if (contentValues.containsKey("amount")) {
                 amount = contentValues.getAsInteger("amount")
             }
             if (contentValues.containsKey("checked")) {
                 checked = contentValues.getAsBoolean("checked")
             }
             if (contentValues.containsKey("shoppingListId")) {
                 shoppingListId = contentValues.getAsLong("shoppingListId")
             }
             return Item(name, price, amount, checked, shoppingListId)
         }
     }
}