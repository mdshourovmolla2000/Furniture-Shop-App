package com.shourov.furnitureshop.view.profilePage.orderHistoryPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shourov.furnitureshop.adapter.OrderDetailsProductListAdapter
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding

    private lateinit var order: OrderTable

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        order = Gson().fromJson(arguments?.getString("DATA", ""), OrderTable::class.java)
        val productListType = object : TypeToken<List<CartTable>>() {}.type
        val productList: List<CartTable> = Gson().fromJson(order.productList, productListType)

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            orderIdTextview.text = order.id.toString()
            orderStatusTextview.text = order.orderStatus
            orderPriceTextview.text = "$${order.itemPrice}"
            orderedOnTextview.text = order.orderDateAndTime
            productRecyclerview.adapter = OrderDetailsProductListAdapter(ArrayList(productList))
        }

        return binding.root
    }
}