package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleProductReviewImageItemLayoutBinding
import com.shourov.furnitureshop.interfaces.ProductReviewImageItemClickListener
import com.shourov.furnitureshop.utils.loadImage

class ProductReviewImageListAdapter(private var itemList: ArrayList<Int>, private val listener: ProductReviewImageItemClickListener):
    RecyclerView.Adapter<ProductReviewImageListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_product_review_image_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleProductReviewImageItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: Int) {
            binding.apply {
                reviewImageImageview.loadImage(currentItem)

                reviewImageImageview.setOnClickListener { listener.onClickProductReviewImageItem(currentItem) }
            }
        }
    }
}