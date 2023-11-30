package com.shourov.furnitureshop.view.cartPage.checkoutPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.databinding.FragmentCheckoutBinding
import com.shourov.furnitureshop.repository.CheckoutRepository
import com.shourov.furnitureshop.utils.NetworkManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.view.MainActivity
import com.shourov.furnitureshop.viewModel.CheckoutViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var repository: CheckoutRepository
    private lateinit var viewModel: CheckoutViewModel

    private var productList = ""
    private var totalAmount = 0.0
    private var shippingFee = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        productList = arguments?.getString("PRODUCT_LIST", "").toString()
        totalAmount = arguments?.getString("TOTAL_AMOUNT", "0.0")!!.toDouble()
        shippingFee = arguments?.getDouble("SHIPPING_FEE", 0.0)!!

        repository = CheckoutRepository(database.appDao())
        viewModel = ViewModelProvider(this, CheckoutViewModelFactory(repository))[CheckoutViewModel::class.java]

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }

            itemTotalAmountTextview.text = "$$totalAmount"
            shippingCostTextview.text = "$$shippingFee"
            totalAmountTextview.text = "$${DecimalFormat("#.##").format(totalAmount+shippingFee)}"

            addressSelectCardView1.setOnClickListener {
                addressSelectIcon2.setImageResource(R.drawable.selection_bullet_unselected)
                addressSelectIcon1.setImageResource(R.drawable.selection_bullet_selected)
            }

            addressSelectCardView2.setOnClickListener {
                addressSelectIcon1.setImageResource(R.drawable.selection_bullet_unselected)
                addressSelectIcon2.setImageResource(R.drawable.selection_bullet_selected)
            }

            paymentSelectCardView1.setOnClickListener {
                paymentSelectIcon2.setImageResource(R.drawable.selection_bullet_unselected)
                paymentSelectIcon1.setImageResource(R.drawable.selection_bullet_selected)
            }

            paymentSelectCardView2.setOnClickListener {
                paymentSelectIcon1.setImageResource(R.drawable.selection_bullet_unselected)
                paymentSelectIcon2.setImageResource(R.drawable.selection_bullet_selected)
            }

            paymentButton.setOnClickListener {
                if (NetworkManager.isInternetAvailable(requireContext())) {
                    try { (activity as MainActivity).viewModel.setLoadingDialogText("Ordering") } catch (_: Exception) { }
                    try { (activity as MainActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy - hh:mm:ss a", Locale.getDefault())
                    val dateWithTime = dateFormat.format(Date())

                    placeOrder(OrderTable(0, productList, DecimalFormat("#.##").format(totalAmount+shippingFee), "Processing", dateWithTime, SharedPref.read("CURRENT_USER_ID", "0")?.toInt()))
                } else {
                    requireContext().showErrorToast("No internet available")
                }
            }
        }


        return binding.root
    }

    private fun placeOrder(order: OrderTable?) {
        viewModel.placeOrder(order) { message ->
            when(message) {
                "Successful" -> {
                    findNavController().navigate(R.id.action_checkoutFragment_to_orderSuccessFragment)
                }
                "Network error" -> requireContext().showErrorToast(message)
                "Something wrong" -> requireContext().showErrorToast(message)
            }

            try { (activity as MainActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }
}





class CheckoutViewModelFactory(private val repository: CheckoutRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CheckoutViewModel(repository) as T
}