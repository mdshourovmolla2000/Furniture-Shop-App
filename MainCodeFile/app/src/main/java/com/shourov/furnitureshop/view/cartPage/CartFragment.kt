package com.shourov.furnitureshop.view.cartPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.ShoppingListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.databinding.FragmentCartBinding
import com.shourov.furnitureshop.interfaces.ShoppingItemClickListener
import com.shourov.furnitureshop.repository.ShoppingRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.viewModel.ShoppingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class CartFragment : Fragment(), ShoppingItemClickListener {

    private lateinit var binding: FragmentCartBinding

    private lateinit var repository: ShoppingRepository
    private lateinit var viewModel: ShoppingViewModel

    private val shoppingItemList = ArrayList<ShoppingTable>()
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
        binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.backIcon.setOnClickListener { hideSelectOption() }

        repository = ShoppingRepository(database.appDao())
        viewModel = ViewModelProvider(this, ShoppingViewModelFactory(repository))[ShoppingViewModel::class.java]

        observerList()

        shoppingListAdapter = ShoppingListAdapter(shoppingItemList, this@CartFragment, selectOptionVisible)

        binding.apply {
            shoppingItemRecyclerview.adapter = shoppingListAdapter

            if (selectOptionVisible) {
                deleteIcon.visibility = View.GONE
                orderSummaryLayout.visibility = View.GONE
                deleteButton.visibility = View.VISIBLE
            } else {
                deleteButton.visibility = View.GONE
                deleteIcon.visibility = View.VISIBLE
                orderSummaryLayout.visibility = View.VISIBLE
            }

            deleteIcon.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.clearShoppingSelection()
                    withContext(Dispatchers.Main) {
                        selectOptionVisible = true
                        shoppingListAdapter.updateSelectOptionVisible(true)
                        deleteIcon.visibility = View.GONE
                        orderSummaryLayout.visibility = View.GONE
                        deleteButton.visibility = View.VISIBLE
                    }
                }
            }

            subtotalAmountTextview.text = "$${DecimalFormat("#.##").format(subTotalAmount)}"
            shippingCostTextview.text = "$${DecimalFormat("#.##").format(shippingFee)}"
            totalPayment = subTotalAmount + shippingFee
            totalPaymentAmountTextview.text = "$${DecimalFormat("#.##").format(totalPayment)}"

            deleteButton.setOnClickListener {
                val selectedShoppingItemList = shoppingItemList.filter { it?.isSelected == true }
                if (selectedShoppingItemList.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.deleteShopping(selectedShoppingItemList)
                        val shoppingItemCount = viewModel.shoppingItemCount(SharedPref.read("CURRENT_USER_ID", "0")?.toInt())
                        withContext(Dispatchers.Main) {
                            if (shoppingItemCount == 0) {
                                selectOptionVisible = false
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observerList() {
        viewModel.getShoppingData(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            shoppingItemList.clear()
            binding.apply {
                if (it.isNullOrEmpty()) {
                    shoppingItemRecyclerview.visibility = View.GONE
                    orderSummaryLayout.visibility = View.GONE
                    deleteIcon.visibility = View.GONE
                    deleteButton.visibility = View.GONE
                    noItemLayout.visibility = View.VISIBLE
                } else {
                    shoppingItemList.addAll(it.reversed())

                    noItemLayout.visibility = View.GONE
                    shoppingItemRecyclerview.visibility = View.VISIBLE

                    viewModel.getSubTotalAmount(shoppingItemList)
                }
                shoppingListAdapter.notifyDataSetChanged()
            }
        }

        viewModel.subTotalAmountLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                subTotalAmount = it
                subtotalAmountTextview.text = "$${DecimalFormat("#.##").format(subTotalAmount)}"
                totalPayment = subTotalAmount + shippingFee
                totalPaymentAmountTextview.text = "$${DecimalFormat("#.##").format(totalPayment)}"
            }
        }
    }

    private fun hideSelectOption() {
        if (selectOptionVisible) {
            selectOptionVisible = false
            shoppingListAdapter.updateSelectOptionVisible(false)
            binding.apply {
                deleteIcon.visibility = View.VISIBLE
                deleteButton.visibility = View.GONE
                orderSummaryLayout.visibility = View.VISIBLE
            }
            lifecycleScope.launch(Dispatchers.IO) { viewModel.clearShoppingSelection() }
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onShoppingItemClick(currentItem: ShoppingTable?, clickOn: String?) {
        when(clickOn) {
            "SELECT_ICON" -> {
                currentItem!!.isSelected = !(currentItem.isSelected)!!
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updateShopping(currentItem)
                }
            }
            "MAIN_ITEM" -> {
                val bundle = bundleOf(
                    "PRODUCT_ID" to currentItem?.productId
                )
                findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, bundle)
            }
            "QUANTITY_PLUS" -> {
                currentItem!!.itemQuantity = currentItem.itemQuantity!! + 1
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updateShopping(currentItem)
                }
            }
            "QUANTITY_MINUS" -> {
                currentItem!!.itemQuantity = currentItem.itemQuantity!! - 1
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updateShopping(currentItem)
                }
            }
        }
    }
}



class ShoppingViewModelFactory(private val repository: ShoppingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ShoppingViewModel(repository) as T
}