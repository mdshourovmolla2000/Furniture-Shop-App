package com.shourov.furnitureshop.view

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
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.AppDatabase
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.databinding.FragmentShoppingBinding
import com.shourov.furnitureshop.interfaces.ShoppingItemClickListener
import com.shourov.furnitureshop.repository.ShoppingRepository
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.view_model.ShoppingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class ShoppingFragment : Fragment(), ShoppingItemClickListener {

    private lateinit var binding: FragmentShoppingBinding

    private lateinit var dao: AppDao
    private lateinit var repository: ShoppingRepository
    private lateinit var viewModel: ShoppingViewModel

    private val shoppingItemList = ArrayList<ShoppingTable?>()
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

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = ShoppingRepository(dao)
        viewModel = ViewModelProvider(this, ShoppingViewModelFactory(repository))[ShoppingViewModel::class.java]

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
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.clearShoppingSelection()
                withContext(Dispatchers.Main) {
                    selectOptionVisible = true
                    shoppingListAdapter.updateSelectOptionVisible(true)
                    binding.deleteIcon.visibility = View.GONE
                    binding.orderSummaryLayout.visibility = View.GONE
                    binding.deleteButton.visibility = View.VISIBLE
                }
            }
        }

        binding.subtotalAmountTextview.text = "$${DecimalFormat("#.##").format(subTotalAmount)}"
        binding.shippingCostTextview.text = "$${DecimalFormat("#.##").format(shippingFee)}"
        totalPayment = subTotalAmount + shippingFee
        binding.totalPaymentAmountTextview.text = "$${DecimalFormat("#.##").format(totalPayment)}"

        binding.deleteButton.setOnClickListener {
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

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observerList() {
        viewModel.getShoppingData(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            shoppingItemList.clear()
            if (it.isNullOrEmpty()) {
                binding.shoppingItemRecyclerview.visibility = View.GONE
                binding.orderSummaryLayout.visibility = View.GONE
                binding.deleteIcon.visibility = View.GONE
                binding.deleteButton.visibility = View.GONE
                binding.noItemLayout.visibility = View.VISIBLE
            } else {
                shoppingItemList.addAll(it.reversed())
                shoppingListAdapter.notifyDataSetChanged()

                binding.noItemLayout.visibility = View.GONE
                binding.shoppingItemRecyclerview.visibility = View.VISIBLE

                viewModel.getSubTotalAmount(shoppingItemList)
            }
        }

        viewModel.subTotalAmountLiveData.observe(viewLifecycleOwner) {
            subTotalAmount = it
            binding.subtotalAmountTextview.text = "$${DecimalFormat("#.##").format(subTotalAmount)}"
            totalPayment = subTotalAmount + shippingFee
            binding.totalPaymentAmountTextview.text = "$${DecimalFormat("#.##").format(totalPayment)}"
        }
    }

    private fun hideSelectOption() {
        if (selectOptionVisible) {
            selectOptionVisible = false
            shoppingListAdapter.updateSelectOptionVisible(false)
            binding.deleteIcon.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.GONE
            binding.orderSummaryLayout.visibility = View.VISIBLE
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.clearShoppingSelection()
            }
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
                findNavController().navigate(R.id.action_shoppingFragment_to_productDetailsFragment, bundle)
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