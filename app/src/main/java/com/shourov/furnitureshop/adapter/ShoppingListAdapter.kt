package com.shourov.furnitureshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.databinding.SingleShoppingItemLayoutBinding
import com.shourov.furnitureshop.interfaces.ShoppingItemClickListener
import com.shourov.furnitureshop.utils.loadImage
import java.text.DecimalFormat

class ShoppingListAdapter(private var itemList: ArrayList<ShoppingTable?>, private val listener: ShoppingItemClickListener, private var selectOptionVisible: Boolean = false):
    RecyclerView.Adapter<ShoppingListAdapter.ItemViewHolder>() {

    fun updateSelectOptionVisible(selectOptionVisible: Boolean) {
        this.selectOptionVisible = selectOptionVisible
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_shopping_item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleShoppingItemLayoutBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: ShoppingTable?) {
            binding.selectIcon.visibility = if (selectOptionVisible) View.VISIBLE else View.GONE

            when(currentItem!!.isSelected) {
                true-> binding.selectIcon.loadImage(R.drawable.cart_select_icon)
                else-> binding.selectIcon.loadImage(R.drawable.cart_unselect_icon)
            }

            binding.itemImageImageview.loadImage(currentItem.itemImage)
            binding.itemNameTextview.text = currentItem.itemName
            binding.itemCompanyTextview.text = currentItem.itemCompany
            binding.itemPriceTextview.text = "$${DecimalFormat("#.##").format(currentItem.itemPrice!! * currentItem.itemQuantity!!)}"
            binding.itemCountTextview.text = currentItem.itemQuantity.toString()

            binding.selectIcon.setOnClickListener {
                currentItem.isSelected = !currentItem.isSelected!!
                notifyDataSetChanged()
            }

            binding.itemQuantityPlusIcon.setOnClickListener {
                listener.onShoppingItemClick(currentItem, "QUANTITY_PLUS")
            }

            binding.itemQuantityMinusIcon.setOnClickListener {
                if (currentItem.itemQuantity!! > 1) {
                    listener.onShoppingItemClick(currentItem, "QUANTITY_MINUS")
                }
            }

            binding.mainCardView.setOnClickListener { listener.onShoppingItemClick(currentItem, "MAIN_ITEM") }
        }
    }
}