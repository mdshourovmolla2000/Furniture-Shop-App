package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.databinding.SingleCartItemLayoutBinding
import com.shourov.furnitureshop.interfaces.CartItemClickListener
import com.shourov.furnitureshop.utils.loadImage
import java.text.DecimalFormat

class CartListAdapter(private var itemList: ArrayList<CartTable>, private val listener: CartItemClickListener, private var selectOptionVisible: Boolean = false):
    RecyclerView.Adapter<CartListAdapter.ItemViewHolder>() {

    fun updateSelectOptionVisible(selectOptionVisible: Boolean) {
        this.selectOptionVisible = selectOptionVisible
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_cart_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleCartItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: CartTable) {
            binding.apply {
                selectIcon.visibility = if (selectOptionVisible) View.VISIBLE else View.GONE
                when(currentItem.isSelected) {
                    true-> selectIcon.setImageResource(R.drawable.cart_select_icon)
                    else-> selectIcon.setImageResource(R.drawable.cart_unselect_icon)
                }
                itemImageImageview.loadImage(currentItem.itemImage)
                itemNameTextview.text = currentItem.itemName
                itemCompanyTextview.text = currentItem.itemCompany
                itemPriceTextview.text = "$${DecimalFormat("#.##").format(currentItem.itemPrice!! * currentItem.itemQuantity!!)}"
                itemCountTextview.text = currentItem.itemQuantity.toString()

                selectIcon.setOnClickListener { listener.onCartItemClick(currentItem, "SELECT_ICON") }

                itemQuantityPlusIcon.setOnClickListener { listener.onCartItemClick(currentItem, "QUANTITY_PLUS") }

                itemQuantityMinusIcon.setOnClickListener {
                    if (currentItem.itemQuantity!! > 1) { listener.onCartItemClick(currentItem, "QUANTITY_MINUS") }
                }

                mainCardView.setOnClickListener { listener.onCartItemClick(currentItem, "MAIN_ITEM") }
            }
        }
    }
}