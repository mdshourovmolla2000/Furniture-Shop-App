package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleHomePopularProductItemLayoutBinding
import com.shourov.furnitureshop.interfaces.PopularProductItemClickListener
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.loadImage

class PopularProductListAdapter(private val itemList: ArrayList<ProductModel>, private val listener: PopularProductItemClickListener):
    RecyclerView.Adapter<PopularProductListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_home_popular_product_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleHomePopularProductItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: ProductModel) {
            binding.apply {
                itemImageImageview.loadImage(currentItem.itemImage)
                itemNameTextview.text = currentItem.itemName
                itemCompanyNameTextview.text = currentItem.itemCompanyName
                itemPriceTextview.text = "$${currentItem.itemPrice}"
            }

            listener.onLoadProductItem(currentItem, binding.cartIconCardView, binding.cartIconImageview)

            itemView.setOnClickListener { listener.onProductItemClick(currentItem, binding.cartIconCardView, "MAIN_ITEM") }

            binding.cartIconCardView.setOnClickListener { listener.onProductItemClick(currentItem, binding.cartIconCardView,"CART_ICON") }
        }
    }
}