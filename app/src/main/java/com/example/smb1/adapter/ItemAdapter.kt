package com.example.smb1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.Item
import com.example.smb1.entity.ItemFirebase


class ItemAdapter(
    private val itemClickListener: AdapterActionSetter<ItemFirebase>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder?>()  {

    private var items: List<ItemFirebase> = emptyList()
    private lateinit var listName: String

    inner class ViewHolder(
        itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        val nameTextView = itemView.findViewById<TextView>(R.id.name)
        val amountTextView = itemView.findViewById<TextView>(R.id.amount)
        val priceTextView = itemView.findViewById<TextView>(R.id.price)
        val checkBox = itemView.findViewById<CheckBox>(R.id.bought)
        init {
            itemView.setOnClickListener(this)
            checkBox.setOnClickListener(this)
            itemClickListener.setFont(listOf(nameTextView, amountTextView, priceTextView))
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION){
                if (v is CheckBox) {
                    itemClickListener.onCheckboxClick(items[adapterPosition], checkBox.isChecked, listName)
                }
                else {
                    itemClickListener.onItemClick(items[adapterPosition], listName, adapterPosition)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val itemView = inflater.inflate(R.layout.list_items, parent, false)
        // Return a new holder instance
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ItemFirebase = items.get(position)
        holder.nameTextView.setText(item.name)
        holder.amountTextView.setText(item.amount.toString())
        holder.priceTextView.setText(item.price.toString())
        holder.checkBox.isChecked = item.checked
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<ItemFirebase>, listName: String) {
        this.items = items
        this.listName = listName
        notifyDataSetChanged()
    }


}
