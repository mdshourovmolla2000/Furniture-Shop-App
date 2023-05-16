package com.shourov.furnitureshop.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.ShoppingListAdapter
import com.shourov.furnitureshop.databinding.FragmentShoppingBinding
import com.shourov.furnitureshop.interfaces.ShoppingItemClickListener
import com.shourov.furnitureshop.model.ShoppingModel
import com.shourov.furnitureshop.repository.ShoppingRepository
import com.shourov.furnitureshop.view_model.ShoppingViewModel

class ShoppingFragment : Fragment(), ShoppingItemClickListener {

    private lateinit var binding: FragmentShoppingBinding
    private lateinit var viewModel: ShoppingViewModel
    private val shoppingItemList: ArrayList<ShoppingModel> = ArrayList()
    private var selectOptionVisible: Boolean = false
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private var subTotalAmount = 0.00
    private val shippingFee = 10.00
    private var totalPayment = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                hideSelectOption()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShoppingBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener { hideSelectOption() }

        viewModel = ViewModelProvider(this, ShoppingViewModelFactory(ShoppingRepository()))[ShoppingViewModel::class.java]

        if (shoppingItemList.isEmpty()) {
            viewModel.getShoppingData()
        }

        observerList()

        shoppingListAdapter = ShoppingListAdapter(shoppingItemList, this@ShoppingFragment, selectOptionVisible)
        binding.shoppingItemRecyclerview.adapter = shoppingListAdapter

        if (selectOptionVisible) {
            binding.deleteIcon.visibility = View.GONE
            binding.orderSummaryLayout.visibility = View.GONE
            binding.deleteButton.visibility = View.VISIBLE
        } else {
            binding.deleteButton.visibility = View.GONE
            binding.deleteIcon.visibility = View.VISIBLE
            binding.orderSummaryLayout.visibility = View.VISIBLE
        }

        binding.deleteIcon.setOnClickListener {
            selectOptionVisible = true
            shoppingListAdapter.updateSelectOptionVisible(true)
            binding.deleteIcon.visibility = View.GONE
            binding.orderSummaryLayout.visibility = View.GONE
            binding.deleteButton.visibility = View.VISIBLE
        }

        binding.subtotalAmountTextview.text = "$${subTotalAmount}"
        binding.shippingCostTextview.text = "$${shippingFee}"
        totalPayment = subTotalAmount + shippingFee
        binding.totalPaymentAmountTextview.text = "$${totalPayment}"

        binding.deleteButton.setOnClickListener {
            val newShoppingItemList = ArrayList(shoppingItemList.filter { it.isSelected == false })
            shoppingItemList.clear()
            shoppingItemList.addAll(newShoppingItemList)
            shoppingListAdapter.notifyDataSetChanged()
            viewModel.getSubTotalAmount(shoppingItemList)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observerList() {
        viewModel.shoppingLiveData.observe(viewLifecycleOwner) {
            shoppingItemList.clear()
            if (it.isEmpty()) {
                binding.shoppingItemRecyclerview.visibility = View.GONE
                binding.orderSummaryLayout.visibility = View.GONE
                binding.deleteIcon.visibility = View.GONE
                binding.noItemLayout.visibility = View.VISIBLE
            } else {
                shoppingItemList.addAll(it)
                shoppingListAdapter.notifyDataSetChanged()

                binding.noItemLayout.visibility = View.GONE
                binding.deleteIcon.visibility = View.VISIBLE
                binding.shoppingItemRecyclerview.visibility = View.VISIBLE

                viewModel.getSubTotalAmount(shoppingItemList)
            }
        }

        viewModel.subTotalAmountLiveData.observe(viewLifecycleOwner) {
            subTotalAmount = it
            binding.subtotalAmountTextview.text = "$${subTotalAmount}"
            totalPayment = subTotalAmount + shippingFee
            binding.totalPaymentAmountTextview.text = "$${totalPayment}"
        }
    }

    private fun hideSelectOption() {
        if (selectOptionVisible) {
            selectOptionVisible = false
            shoppingListAdapter.updateSelectOptionVisible(false)
            binding.deleteIcon.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.GONE
            binding.orderSummaryLayout.visibility = View.VISIBLE
            shoppingItemList.forEach {
                it.isSelected = false
            }
            shoppingListAdapter.notifyDataSetChanged()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onShoppingItemClick(currentItem: ShoppingModel?, clickOn: String?) {
        when(clickOn) {
            "MAIN_ITEM" -> findNavController().navigate(R.id.action_shoppingFragment_to_productDetailsFragment)
            "QUANTITY_PLUS" -> {
                currentItem!!.itemQuantity = currentItem.itemQuantity!! + 1
                shoppingListAdapter.notifyDataSetChanged()
                viewModel.getSubTotalAmount(shoppingItemList)
            }
            "QUANTITY_MINUS" -> {
                currentItem!!.itemQuantity = currentItem.itemQuantity!! - 1
                shoppingListAdapter.notifyDataSetChanged()
                viewModel.getSubTotalAmount(shoppingItemList)
            }
        }
    }
}



class ShoppingViewModelFactory(private val repository: ShoppingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ShoppingViewModel(repository) as T
}