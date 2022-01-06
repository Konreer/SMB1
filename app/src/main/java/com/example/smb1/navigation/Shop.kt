package com.example.smb1.navigation

import android.content.ContentValues
import com.example.smb1.entity.Item
import com.google.android.gms.maps.model.LatLng

class Shop(var name: String, var description: String, var radius: Double, var checked: Boolean, var latitude: Double, var longitude: Double) {

    constructor() :this("", "", 0.0, true, 0.0,0.0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shop

        if (name != other.name) return false
        if (description != other.description) return false
        if (radius != other.radius) return false
        if (checked != other.checked) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + checked.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }


//    companion object {
//        fun fromContentValues(contentValues: ContentValues): Item? {
//            var name: String = ""
//            var price: Double = 0.0
//            var amount: Int = 0
//            var checked: Boolean = false
//            var shoppingListId: Long = -1
//
//            if (contentValues.containsKey("name")) {
//                name = contentValues.getAsString("name")
//            }
//            if (contentValues.containsKey("price")) {
//                price = contentValues.getAsDouble("price")
//            }
//            if (contentValues.containsKey("amount")) {
//                amount = contentValues.getAsInteger("amount")
//            }
//            if (contentValues.containsKey("checked")) {
//                checked = contentValues.getAsBoolean("checked")
//            }
//            if (contentValues.containsKey("shoppingListId")) {
//                shoppingListId = contentValues.getAsLong("shoppingListId")
//            }
//            return Item(name, price, amount, checked, shoppingListId)
//        }
//    }
}