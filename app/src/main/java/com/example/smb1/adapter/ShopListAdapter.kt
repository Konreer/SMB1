package com.example.smb1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.navigation.Shop


class ShopListAdapter(
    private val itemClickListener: AdapterActionSetter<Shop>
) : RecyclerView.Adapter<ShopListAdapter.ViewHolder?>()  {

    private var items: List<Shop> = emptyList()
    private lateinit var listName: String

    inner class ViewHolder(
        itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        val nameTextView = itemView.findViewById<TextView>(R.id.shopName)
        val descriptionTextView = itemView.findViewById<TextView>(R.id.description)
        val radiusTextView = itemView.findViewById<TextView>(R.id.radius)
        val checkBox = itemView.findViewById<CheckBox>(R.id.favourite)
        init {
            itemView.setOnClickListener(this)
            checkBox.setOnClickListener(this)
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val itemView = inflater.inflate(R.layout.list_shops, parent, false)
        // Return a new holder instance
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Shop = items.get(position)
        holder.nameTextView.setText(item.name)
        holder.descriptionTextView.setText(item.description)
        holder.radiusTextView.setText(item.radius.toString())
        holder.checkBox.isChecked = item.checked
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Shop>, listName: String) {
        this.items = items
        this.listName = listName
        notifyDataSetChanged()
    }


}
