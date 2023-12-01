package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleProductReviewItemLayoutBinding
import com.shourov.furnitureshop.interfaces.ProductReviewItemClickListener
import com.shourov.furnitureshop.model.ProductReviewModel

class ProductReviewListAdapter(private var itemList: ArrayList<ProductReviewModel>, private val listener: ProductReviewItemClickListener):
    RecyclerView.Adapter<ProductReviewListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_product_review_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleProductReviewItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: ProductReviewModel) {
            binding.apply {
                userNameTextview.text = currentItem.userName
                reviewTimeTextview.text = currentItem.reviewDateAndTime
                when (currentItem.reviewRating) {
                    0 -> ratingStarImageview.setImageResource(R.drawable.rating_star_blank)
                    1 -> ratingStarImageview.setImageResource(R.drawable.rating_star_1)
                    2 -> ratingStarImageview.setImageResource(R.drawable.rating_star_2)
                    3 -> ratingStarImageview.setImageResource(R.drawable.rating_star_3)
                    4 -> ratingStarImageview.setImageResource(R.drawable.rating_star_4)
                    5 -> ratingStarImageview.setImageResource(R.drawable.rating_star_5)
                }

                if (currentItem.reviewImages.isNullOrEmpty()) {
                    reviewImageRecyclerview.visibility = View.GONE
                } else {
                    reviewImageRecyclerview.visibility = View.VISIBLE
                }

                if (currentItem.reviewDetails.isNullOrEmpty()) {
                    reviewDetailsTextview.visibility = View.GONE
                } else {
                    reviewDetailsTextview.visibility = View.VISIBLE
                }

                reviewDetailsTextview.text = currentItem.reviewDetails

                listener.onLoadReviewItem(currentItem, reviewImageRecyclerview)
            }
        }
    }
}