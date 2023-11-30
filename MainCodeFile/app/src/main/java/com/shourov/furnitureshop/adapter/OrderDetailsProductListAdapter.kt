package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.databinding.SingleOrderDetailsProductItemLayoutBinding
import com.shourov.furnitureshop.utils.loadImage

class OrderDetailsProductListAdapter(private var itemList: ArrayList<CartTable>):
    RecyclerView.Adapter<OrderDetailsProductListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_order_details_product_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleOrderDetailsProductItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: CartTable) {
            binding.apply {
                productImage.loadImage(currentItem.itemImage)
                productNameTextview.text = currentItem.itemName
                productCompanyNameTextview.text = currentItem.itemCompany
                productPriceTextview.text = "$${currentItem.itemPrice} X ${currentItem.itemQuantity}"
            }
        }
    }
}