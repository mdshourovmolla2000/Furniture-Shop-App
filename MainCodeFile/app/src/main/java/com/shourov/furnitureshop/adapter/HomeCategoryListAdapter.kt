package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleHomeCategoryItemLayoutBinding
import com.shourov.furnitureshop.interfaces.HomeCategoryItemClickListener
import com.shourov.furnitureshop.model.HomeCategoryModel

class HomeCategoryListAdapter(private val itemList: ArrayList<HomeCategoryModel>, currentCategoryPosition: Int, private val itemClickListener: HomeCategoryItemClickListener):
    RecyclerView.Adapter<HomeCategoryListAdapter.ItemViewHolder>() {

    private var currentIndex = currentCategoryPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_home_category_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position], position)

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleHomeCategoryItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: HomeCategoryModel, position: Int) {
            binding.apply {
                categoryImageImageview.setImageResource(currentItem.categoryImage!!)
                categoryNameTextview.text = currentItem.categoryName

                //selecting bg color of current selected item
                if (currentIndex == position) {
                    categoryNameCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.themeColor))
                    categoryImageImageview.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN)
                    categoryNameTextview.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                } else {
                    categoryNameCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    categoryImageImageview.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
                    categoryNameTextview.setTextColor(Color.parseColor("#828A89"))
                }
            }



            itemView.setOnClickListener {
                when(currentItem.categoryName) {
                    "More" -> itemClickListener.onCategoryItemClick("More", position)
                    else -> {
                        currentIndex = position
                        itemClickListener.onCategoryItemClick(currentItem.categoryName!!, position)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}