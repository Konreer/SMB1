package com.example.smb1.adapter.utils

import android.widget.TextView

interface AdapterActionSetter<L> {
    fun onItemClick(item: L, listName: String, index: Int)
    fun onCheckboxClick(item: L, isChecked: Boolean, listName: String) {
        //do nothing on default
    }
    fun setFont(list: List<TextView>)
}