package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleCategoryProductItemLayoutBinding
import com.shourov.furnitureshop.interfaces.CategoryProductItemClickListener
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.utils.loadImage

class CategoryProductListAdapter(private val itemList: ArrayList<ProductModel>, private val listener: CategoryProductItemClickListener):
    RecyclerView.Adapter<CategoryProductListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_category_product_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleCategoryProductItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: ProductModel) {
            binding.apply {
                productImage.loadImage(currentItem.itemImage)
                productNameTextview.text = currentItem.itemName
                productCompanyNameTextview.text = currentItem.itemCompanyName
                productPriceTextview.text = "$${currentItem.itemPrice}"
            }

            listener.onLoadProductItem(currentItem, binding.cartIconCardView, binding.cartIconImageview)

            itemView.setOnClickListener { listener.onProductItemClick(currentItem, binding.cartIconCardView, "MAIN_ITEM") }

            binding.cartIconCardView.setOnClickListener { listener.onProductItemClick(currentItem, binding.cartIconCardView,"CART_ICON") }
        }
    }
}