package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.databinding.SingleOrderItemLayoutBinding
import com.shourov.furnitureshop.interfaces.OrderItemClickListener

class OrderListAdapter(private var itemList: ArrayList<OrderTable>, private val listener: OrderItemClickListener):
    RecyclerView.Adapter<OrderListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_order_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleOrderItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: OrderTable) {
            binding.apply {
                orderIdTextview.text = "Order id: ${currentItem.id}"
                orderedOnTextview.text = "Ordered on: ${currentItem.orderDateAndTime}"
                itemPriceTextview.text = "Price: $${currentItem.itemPrice}"
            }

            itemView.setOnClickListener { listener.onOrderItemClick(currentItem) }
        }

    }
}