package com.example.smb1.entity

import android.content.ContentValues
import java.util.*

data class ShoppingListFirebase(var date: Date) {
    var items: List<ItemFirebase> = emptyList()

    constructor() : this(Date()) {
        items = emptyList()
    }

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