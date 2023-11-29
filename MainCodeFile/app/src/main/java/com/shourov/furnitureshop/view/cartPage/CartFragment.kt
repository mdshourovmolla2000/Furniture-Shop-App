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
import com.shourov.furnitureshop.adapter.CartListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.databinding.FragmentCartBinding
import com.shourov.furnitureshop.interfaces.CartItemClickListener
import com.shourov.furnitureshop.repository.CartRepository
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showInfoToast
import com.shourov.furnitureshop.viewModel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class CartFragment : Fragment(), CartItemClickListener {

    private lateinit var binding: FragmentCartBinding

    private lateinit var repository: CartRepository
    private lateinit var viewModel: CartViewModel

    private val cartItemList = ArrayList<CartTable>()
    private var selectOptionVisible: Boolean = false
    private lateinit var cartListAdapter: CartListAdapter
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

        repository = CartRepository(database.appDao())
        viewModel = ViewModelProvider(this, CartViewModelFactory(repository))[CartViewModel::class.java]

        observerList()

        cartListAdapter = CartListAdapter(cartItemList, this, selectOptionVisible)

        binding.apply {
            backIcon.setOnClickListener { hideSelectOption() }
            cartItemRecyclerview.adapter = cartListAdapter

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
                viewModel.clearCartSelection()
                selectOptionVisible = true
                cartListAdapter.updateSelectOptionVisible(true)
                deleteIcon.visibility = View.GONE
                orderSummaryLayout.visibility = View.GONE
                deleteButton.visibility = View.VISIBLE
            }

            subtotalAmountTextview.text = "$${DecimalFormat("#.##").format(subTotalAmount)}"
            shippingCostTextview.text = "$${DecimalFormat("#.##").format(shippingFee)}"
            totalPayment = subTotalAmount + shippingFee
            totalPaymentAmountTextview.text = "$${DecimalFormat("#.##").format(totalPayment)}"

            deleteButton.setOnClickListener {
                val selectedShoppingItemList = cartItemList.filter { it.isSelected!! }
                if (selectedShoppingItemList.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.deleteCart(selectedShoppingItemList)
                        val shoppingItemCount = viewModel.cartItemCount(SharedPref.read("CURRENT_USER_ID", "0")?.toInt())
                        withContext(Dispatchers.Main) {
                            if (shoppingItemCount == 0) {
                                selectOptionVisible = false
                            }
                        }
                    }
                } else {
                    requireContext().showInfoToast("Select item first")
                }
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observerList() {
        viewModel.getCartData(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            cartItemList.clear()
            binding.apply {
                if (it.isNullOrEmpty()) {
                    selectOptionVisible = false
                    cartItemRecyclerview.visibility = View.GONE
                    orderSummaryLayout.visibility = View.GONE
                    deleteIcon.visibility = View.GONE
                    deleteButton.visibility = View.GONE
                    noItemLayout.visibility = View.VISIBLE
                } else {
                    cartItemList.addAll(it.reversed())

                    noItemLayout.visibility = View.GONE
                    cartItemRecyclerview.visibility = View.VISIBLE

                    viewModel.getSubTotalAmount(cartItemList)
                }
                cartListAdapter.notifyDataSetChanged()
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
            cartListAdapter.updateSelectOptionVisible(false)
            binding.apply {
                deleteIcon.visibility = View.VISIBLE
                deleteButton.visibility = View.GONE
                orderSummaryLayout.visibility = View.VISIBLE
            }
            viewModel.clearCartSelection()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onCartItemClick(currentItem: CartTable?, clickedOn: String?) {
        when(clickedOn) {
            "SELECT_ICON" -> {
                currentItem!!.isSelected = !(currentItem.isSelected)!!
                viewModel.updateCart(currentItem)
            }
            "MAIN_ITEM" -> {
                if (NetworkManager.isInternetAvailable(requireContext())) {
                    val bundle = bundleOf(
                        "PRODUCT_ID" to currentItem?.productId
                    )
                    findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, bundle)
                } else {
                    requireContext().showErrorToast("No internet available")
                }
            }
            "QUANTITY_PLUS" -> {
                currentItem!!.itemQuantity = currentItem.itemQuantity!! + 1
                viewModel.updateCart(currentItem)
            }
            "QUANTITY_MINUS" -> {
                currentItem!!.itemQuantity = currentItem.itemQuantity!! - 1
                viewModel.updateCart(currentItem)
            }
        }
    }
}




class CartViewModelFactory(private val repository: CartRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CartViewModel(repository) as T
}