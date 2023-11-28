package com.shourov.furnitureshop.view.profilePage

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.adapter.AddressListAdapter
import com.shourov.furnitureshop.application.BaseApplication.Companion.database
import com.shourov.furnitureshop.database.tables.AddressTable
import com.shourov.furnitureshop.databinding.DialogAddAddressBinding
import com.shourov.furnitureshop.databinding.DialogUpdateAddressBinding
import com.shourov.furnitureshop.databinding.FragmentDeliveryAddressBinding
import com.shourov.furnitureshop.interfaces.AddressItemClickListener
import com.shourov.furnitureshop.repository.DeliveryAddressRepository
import com.shourov.furnitureshop.utils.KeyboardManager
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.utils.showErrorToast
import com.shourov.furnitureshop.utils.showSuccessToast
import com.shourov.furnitureshop.view.authPage.AuthActivity
import com.shourov.furnitureshop.viewModel.DeliveryAddressViewModel

class DeliveryAddressFragment : Fragment(), AddressItemClickListener {

    private lateinit var binding: FragmentDeliveryAddressBinding

    private lateinit var repository: DeliveryAddressRepository
    private lateinit var viewModel: DeliveryAddressViewModel

    private val addressList = ArrayList<AddressTable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryAddressBinding.inflate(inflater, container, false)

        repository = DeliveryAddressRepository(database.appDao())
        viewModel = ViewModelProvider(this, DeliveryAddressViewModelFactory(repository))[DeliveryAddressViewModel::class.java]

        observerList()

        binding.apply {
            backIcon.setOnClickListener { findNavController().popBackStack() }
            addIcon.setOnClickListener { addAddressDialog() }
            addressRecyclerview.adapter = AddressListAdapter(addressList, this@DeliveryAddressFragment)
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.getAddressData(SharedPref.read("CURRENT_USER_ID", "0")?.toInt()).observe(viewLifecycleOwner) {
            addressList.clear()
            binding.apply {
                if (it.isNullOrEmpty()) {
                    addressRecyclerview.visibility = View.GONE
                    noItemLayout.visibility = View.VISIBLE
                } else {
                    addressList.addAll(it.reversed())

                    noItemLayout.visibility = View.GONE
                    addressRecyclerview.visibility = View.VISIBLE
                }

                addressRecyclerview.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun addAddressDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = DialogAddAddressBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        builder.setCancelable(true)

        val alertDialog = builder.create()

        //make transparent to default dialog
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

        dialogBinding.apply {
            saveButton.setOnClickListener {
                if (dialogBinding.fullNameEdittext.text.toString().trim().isEmpty()) {
                    dialogBinding.fullNameEdittext.error = "Enter full name"
                    dialogBinding.fullNameEdittext.requestFocus()
                    return@setOnClickListener
                }
                if (dialogBinding.mobileNumberEdittext.text.toString().trim().isEmpty()) {
                    dialogBinding.mobileNumberEdittext.error = "Enter mobile number"
                    dialogBinding.mobileNumberEdittext.requestFocus()
                    return@setOnClickListener
                }
                if (dialogBinding.fullAddressEdittext.text.toString().trim().isEmpty()) {
                dialogBinding.fullAddressEdittext.error = "Enter full address"
                dialogBinding.fullAddressEdittext.requestFocus()
                return@setOnClickListener
                }

                KeyboardManager.hideKeyBoard(requireContext(), it)
                try { (activity as AuthActivity).viewModel.setLoadingDialogText("Adding address") } catch (_: Exception) { }
                try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

                insertAddress(AddressTable(0, dialogBinding.fullNameEdittext.text.toString().trim(), dialogBinding.mobileNumberEdittext.text.toString().trim(), dialogBinding.fullAddressEdittext.text.toString().trim(), SharedPref.read("CURRENT_USER_ID", "0")?.toInt()), alertDialog)
            }
        }

        alertDialog.show()
    }

    private fun insertAddress(address: AddressTable?, alertDialog: AlertDialog) {
        viewModel.insertAddress(address) { message ->
            when(message) {
                "Address added" -> {
                    requireContext().showSuccessToast(message)
                    alertDialog.dismiss()
                }
                "Something wrong" -> {
                    requireContext().showErrorToast(message)
                }
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }

    private fun updateAddress(address: AddressTable, alertDialog: AlertDialog) {
        viewModel.updateAddress(address) { message ->
            when(message) {
                "Updated" -> {
                    requireContext().showSuccessToast(message)
                    alertDialog.dismiss()
                }
                "Something wrong" -> {
                    requireContext().showErrorToast(message)
                }
            }

            try { (activity as AuthActivity).viewModel.setLoadingDialog(false) } catch (_: Exception) { }
        }
    }

    override fun onAddressItemClick(currentItem: AddressTable, clickedOn: String) {
        when(clickedOn) {
            "DELETE_ICON" -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmation")
                builder.setIcon(R.mipmap.ic_launcher)
                builder.setMessage("Do You Really Want To Delete This?\n\n${currentItem.fullName}\n${currentItem.mobileNumber}\n${currentItem.fullAddress}")
                builder.setPositiveButton("Yes") {dialog, _ ->
                    viewModel.deleteAddress(currentItem)
                    dialog.dismiss()
                }
                builder.setNegativeButton("No") {dialog, _ ->
                    dialog.dismiss()
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
            "EDIT_ICON" -> {
                val builder = AlertDialog.Builder(requireContext())
                val dialogBinding = DialogUpdateAddressBinding.inflate(layoutInflater)

                builder.setView(dialogBinding.root)
                builder.setCancelable(true)

                val alertDialog = builder.create()

                //make transparent to default dialog
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

                dialogBinding.apply {
                    dialogBinding.fullNameEdittext.setText(currentItem.fullName)
                    dialogBinding.mobileNumberEdittext.setText(currentItem.mobileNumber)
                    dialogBinding.fullAddressEdittext.setText(currentItem.fullAddress)

                    updateButton.setOnClickListener {
                        if (dialogBinding.fullNameEdittext.text.toString().trim().isEmpty()) {
                            dialogBinding.fullNameEdittext.error = "Enter full name"
                            dialogBinding.fullNameEdittext.requestFocus()
                            return@setOnClickListener
                        }
                        if (dialogBinding.mobileNumberEdittext.text.toString().trim().isEmpty()) {
                            dialogBinding.mobileNumberEdittext.error = "Enter mobile number"
                            dialogBinding.mobileNumberEdittext.requestFocus()
                            return@setOnClickListener
                        }
                        if (dialogBinding.fullAddressEdittext.text.toString().trim().isEmpty()) {
                            dialogBinding.fullAddressEdittext.error = "Enter full address"
                            dialogBinding.fullAddressEdittext.requestFocus()
                            return@setOnClickListener
                        }

                        KeyboardManager.hideKeyBoard(requireContext(), it)
                        try { (activity as AuthActivity).viewModel.setLoadingDialogText("Updating address") } catch (_: Exception) { }
                        try { (activity as AuthActivity).viewModel.setLoadingDialog(true) } catch (_: Exception) { }

                        currentItem.fullName = dialogBinding.fullNameEdittext.text.toString().trim()
                        currentItem.mobileNumber = dialogBinding.mobileNumberEdittext.text.toString().trim()
                        currentItem.fullAddress = dialogBinding.fullAddressEdittext.text.toString().trim()

                        updateAddress(currentItem, alertDialog)
                    }
                }

                alertDialog.show()
            }
        }
    }
}




class DeliveryAddressViewModelFactory(private val repository: DeliveryAddressRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DeliveryAddressViewModel(repository) as T
}