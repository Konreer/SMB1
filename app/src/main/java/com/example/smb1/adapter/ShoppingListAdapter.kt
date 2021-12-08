package com.example.smb1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.R
import com.example.smb1.adapter.utils.AdapterActionSetter
import com.example.smb1.entity.ShoppingList
import com.example.smb1.entity.ShoppingListFirebase
import com.example.smb1.relation.ShoppingListWithItems

class ShoppingListAdapter(
    private val itemClickListener: AdapterActionSetter<ShoppingListFirebase>
    )
    : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder?>(){
    private var shoppingLists: List<ShoppingListFirebase> = emptyList()
    private lateinit var listName: String

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        val dateTextView = itemView.findViewById<TextView>(R.id.date)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION){
                itemClickListener.onItemClick(shoppingLists[adapterPosition], shoppingLists[adapterPosition].date.toString(), adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val itemView = inflater.inflate(R.layout.list_shopping_lists, parent, false)
        // Return a new holder instance
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list: ShoppingListFirebase = shoppingLists.get(position)
        holder.dateTextView.setText(list.date.toString())
        itemClickListener.setFont(listOf(holder.dateTextView))
    }

    override fun getItemCount(): Int {
        return shoppingLists.size
    }

    fun setLists(shoppingLists: List<ShoppingListFirebase>) {
        this.shoppingLists = shoppingLists
        notifyDataSetChanged()
    }

}