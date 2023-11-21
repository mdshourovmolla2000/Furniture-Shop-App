package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.databinding.SingleFavouriteItemLayoutBinding
import com.shourov.furnitureshop.interfaces.FavouriteItemClickListener

class FavouriteListAdapter(private var itemList: ArrayList<FavouriteTable>, private val listener: FavouriteItemClickListener):
    RecyclerView.Adapter<FavouriteListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_favourite_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleFavouriteItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: FavouriteTable) {
            listener.onLoadFavouriteItem(currentItem.productId, binding.itemImageImageview, binding.itemNameTextview, binding.itemPriceTextview)

            binding.favouriteIcon.setOnClickListener { listener.onFavouriteItemClick(currentItem, "FAVOURITE_ICON") }

            itemView.setOnClickListener { listener.onFavouriteItemClick(currentItem, "MAIN_ITEM") }
        }

    }
}