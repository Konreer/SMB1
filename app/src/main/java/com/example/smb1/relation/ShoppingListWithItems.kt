package com.example.smb1.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.smb1.entity.Item
import com.example.smb1.entity.ShoppingList

data class ShoppingListWithItems(
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "shoppingListId",
        entityColumn = "itemId"
    )
    val Items: List<Item>
) {
}