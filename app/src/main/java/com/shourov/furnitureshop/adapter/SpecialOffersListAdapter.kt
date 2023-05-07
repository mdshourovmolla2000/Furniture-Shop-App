package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.SingleSpecialOfferItemLayoutBinding
import com.shourov.furnitureshop.model.SpecialOfferData
import com.shourov.furnitureshop.utils.loadImage

class SpecialOffersListAdapter(private var itemList: ArrayList<SpecialOfferData>):
    RecyclerView.Adapter<SpecialOffersListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_special_offer_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleSpecialOfferItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: SpecialOfferData) {
            binding.itemImageview.loadImage(currentItem.itemImage)

            binding.discountTitleTextview.text = currentItem.discountTitle
            binding.discountTaglineTextview.text = currentItem.discountTagline
            binding.actionButton.text = currentItem.actionText

        }

    }
}