package com.example.smb1.entity

import android.content.ContentValues


data class ItemFirebase(var name: String, var price: Double, var amount: Int, var checked: Boolean) {

    constructor() : this("", 0.0, 0, false){

    }



    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemFirebase

        if (name != other.name || price != other.price || amount != other.amount) return false

        return true
    }

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
