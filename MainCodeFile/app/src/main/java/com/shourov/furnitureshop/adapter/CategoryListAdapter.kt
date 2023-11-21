package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleCategoryItemLayoutBinding
import com.shourov.furnitureshop.interfaces.CategoryItemClickListener
import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.utils.loadImage

class CategoryListAdapter(private val itemList: ArrayList<CategoryModel>, private val listener: CategoryItemClickListener):
    RecyclerView.Adapter<CategoryListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_category_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleCategoryItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: CategoryModel) {
            binding.apply {
                categoryImage.loadImage(currentItem.categoryImage)
                categoryNameTextview.text = currentItem.categoryName
            }

            listener.onLoadCategoryItem(currentItem, binding.categoryProductCountTextview)

            itemView.setOnClickListener { listener.onCategoryItemClick(currentItem.categoryName) }
        }
    }
}