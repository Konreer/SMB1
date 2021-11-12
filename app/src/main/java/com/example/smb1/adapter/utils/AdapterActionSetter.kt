package com.example.smb1.adapter.utils

import android.widget.TextView

interface AdapterActionSetter<L> {
    fun onItemClick(item: L)
    fun onCheckboxClick(item: L, isChecked: Boolean) {
        //do nothing on default
    }
    fun setFont(list: List<TextView>)
}