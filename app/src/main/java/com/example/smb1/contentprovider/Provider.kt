package com.example.smb1.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.smb1.database.DB
import android.content.UriMatcher
import com.example.smb1.dao.ItemDao
import com.example.smb1.dao.ShoppingListDao
import java.lang.IllegalArgumentException
import android.content.ContentUris
import com.example.smb1.entity.Item
import com.example.smb1.entity.ShoppingList


class Provider : ContentProvider() {

    val TAG: String = Provider::class.java.getName()
    private var itemDao: ItemDao? = null
    private var shoppingListDao: ShoppingListDao? = null
    /**
     * Authority of this content provider
     */
    val AUTHORITY = "com.example.smb1.provider"
    val ITEM_TABLE_NAME = "item"
    val SHOPPING_LIST_TABLE_NAME = "shopping_list"
    /**
     * The match code for some items in the Person table
     */
    val ID_ITEM_DATA = 1
    val ID_SHOPPING_LIST_DATA = 2
    /**
     * The match code for an item in the PErson table
     */
    val ID_ITEM_DATA_ITEM = 111
    val ID_SHOPPING_LIST_DATA_ITEM = 112

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init{
        uriMatcher.addURI(
            AUTHORITY,
            ITEM_TABLE_NAME,
            ID_ITEM_DATA
        )
        uriMatcher.addURI(
            AUTHORITY,
            ITEM_TABLE_NAME +
                    "/*", ID_ITEM_DATA_ITEM
        )
        uriMatcher.addURI(
            AUTHORITY,
            SHOPPING_LIST_TABLE_NAME,
            ID_SHOPPING_LIST_DATA
        )
        uriMatcher.addURI(
            AUTHORITY,
            ITEM_TABLE_NAME +
                    "/*", ID_SHOPPING_LIST_DATA_ITEM
        )
    }
    override fun onCreate(): Boolean {
        itemDao = DB.getDatabase(context!!).ItemDao()
        shoppingListDao = DB.getDatabase(context!!).ShoppingListDao()
        return false;
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            ID_ITEM_DATA_ITEM -> {
                cursor = itemDao!!.getAllItemsAsCursor()
                if (context != null) {
                    cursor.setNotificationUri(context!!.contentResolver, uri)
                    return cursor
                }
                throw IllegalArgumentException("Unknown URI: $uri")
            }
            ID_SHOPPING_LIST_DATA_ITEM -> {
                cursor = shoppingListDao!!.getAllShoppingListAsCursor()
                if (context != null) {
                    cursor.setNotificationUri(context!!.contentResolver, uri)
                    return cursor
                }
                throw IllegalArgumentException("Unknown URI: $uri")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return null;
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            ID_ITEM_DATA -> {
                if (context != null) {
                    val id: Long? = values?.let { Item.fromContentValues(it)?.let { itemDao?.insert(it) } }
                    if (id != 0L) {
                        context!!.contentResolver
                            .notifyChange(uri, null)
                        return ContentUris.withAppendedId(uri, id!!)
                    }
                }
                throw IllegalArgumentException("Invalid URI: Insert failed$uri")
            }
            ID_SHOPPING_LIST_DATA -> {
                if (context != null) {
                    val id: Long? = values?.let { ShoppingList.fromContentValues(it)?.let { shoppingListDao?.insert(it) } }
                    if (id != 0L) {
                        context!!.contentResolver
                            .notifyChange(uri, null)
                        return ContentUris.withAppendedId(uri, id!!)
                    }
                }
                throw IllegalArgumentException("Invalid URI: Insert failed$uri")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")

        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            ID_ITEM_DATA_ITEM -> {
                if (context != null) {
                    val count: Int = (itemDao?.delete(ContentUris.parseId(uri)) ?: context!!.contentResolver
                        .notifyChange(uri, null)) as Int
                    return count
                }
                throw IllegalArgumentException("Unknown URI:$uri")
            }
            ID_SHOPPING_LIST_DATA_ITEM -> {
                if (context != null) {
                    val count: Int = (shoppingListDao?.delete(ContentUris.parseId(uri)) ?: context!!.contentResolver
                        .notifyChange(uri, null)) as Int
                    return count
                }
                throw IllegalArgumentException("Unknown URI:$uri")
            }
            else -> throw IllegalArgumentException("Unknown URI:$uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when (uriMatcher.match(uri)) {
            ID_ITEM_DATA -> {
                if (context != null) {
                    val count: Int? = values?.let {
                        Item.fromContentValues(it)?.let {
                            itemDao?.update(it) } }
                    if (count != 0) {
                        context!!.contentResolver
                            .notifyChange(uri, null)
                        return count!!
                    }
                }
                throw IllegalArgumentException("Invalid URI:  cannot update")
            }
            ID_SHOPPING_LIST_DATA -> {
                if (context != null) {
                    val count: Int? = values?.let {
                        ShoppingList.fromContentValues(it)?.let {
                            shoppingListDao?.update(it) } }
                    if (count != 0) {
                        context!!.contentResolver
                            .notifyChange(uri, null)
                        return count!!
                    }
                }
                throw IllegalArgumentException("Invalid URI:  cannot update")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

}
