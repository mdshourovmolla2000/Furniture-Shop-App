package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.AddressTable
import com.shourov.furnitureshop.databinding.SingleAddressItemLayoutBinding
import com.shourov.furnitureshop.interfaces.AddressItemClickListener

class AddressListAdapter(private val itemList: ArrayList<AddressTable>, private val listener: AddressItemClickListener):
    RecyclerView.Adapter<AddressListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_address_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleAddressItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: AddressTable) {
            binding.apply {
                nameTextview.text = currentItem.fullName
                mobileNumberTextview.text = currentItem.mobileNumber
                addressTextview.text = currentItem.fullAddress
            }

            //itemView.setOnClickListener { listener.onCategoryItemClick(currentItem.categoryName!!) }
        }
    }
}