package com.shourov.furnitureshop.view.profilePage.orderHistoryPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.OrderListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.databinding.FragmentOrderProcessingBinding
import com.shourov.furnitureshop.interfaces.OrderItemClickListener
import com.shourov.furnitureshop.repository.OrderProcessingRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.viewModel.OrderProcessingViewModel

class OrderProcessingFragment : Fragment(), OrderItemClickListener {

    private lateinit var binding: FragmentOrderProcessingBinding

    private lateinit var repository: OrderProcessingRepository
    private lateinit var viewModel: OrderProcessingViewModel

    private val orderList = ArrayList<OrderTable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderProcessingBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = View.VISIBLE

        repository = OrderProcessingRepository(database.appDao())
        viewModel = ViewModelProvider(this, OrderProcessingViewModelFactory(repository))[OrderProcessingViewModel::class.java]

        observerList()

        binding.orderProcessingItemRecyclerview.adapter = OrderListAdapter(orderList, this)

        return binding.root
    }

    private fun observerList() {
        viewModel.getOrderData(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            orderList.clear()
            binding.apply {
                if (it.isNullOrEmpty()) {
                    orderProcessingItemRecyclerview.visibility = View.GONE
                    noItemLayout.visibility = View.VISIBLE
                } else {
                    orderList.addAll(it.reversed())

                    noItemLayout.visibility = View.GONE
                    orderProcessingItemRecyclerview.visibility = View.VISIBLE
                }

                orderProcessingItemRecyclerview.adapter?.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onOrderItemClick(currentItem: OrderTable) {
        val bundle = bundleOf(
            "DATA" to Gson().toJson(currentItem)
        )

        findNavController().navigate(R.id.action_orderHistoryFragment_to_orderDetailsFragment, bundle)
    }
}






class OrderProcessingViewModelFactory(private val repository: OrderProcessingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = OrderProcessingViewModel(repository) as T
}