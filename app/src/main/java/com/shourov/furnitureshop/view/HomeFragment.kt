package com.shourov.furnitureshop.view

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
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
import com.shourov.furnitureshop.adapter.SpecialOffersListAdapter
import com.shourov.furnitureshop.databinding.DialogExitBinding
import com.shourov.furnitureshop.databinding.FragmentHomeBinding
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.repository.HomeRepository
import com.shourov.furnitureshop.view_model.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel

    private val specialOfferItemsList: ArrayList<SpecialOfferModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                val dialogBinding = DialogExitBinding.inflate(layoutInflater)

                builder.setView(dialogBinding.root)
                builder.setCancelable(true)

                val alertDialog = builder.create()

                //make transparent to default dialog
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

                dialogBinding.noButton.setOnClickListener { alertDialog.dismiss() }

                dialogBinding.yesButton.setOnClickListener {
                    alertDialog.dismiss()
                    requireActivity().finish()
                }

                alertDialog.show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepository()))[HomeViewModel::class.java]

        viewModel.getSpecialOfferData()

        observerList()

        binding.specialOfferRecyclerview.apply {
            adapter = SpecialOffersListAdapter(specialOfferItemsList)
        }

        binding.bottomNavigationFavouriteMenu.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment) }

        return binding.root
    }

    private fun observerList() {
        viewModel.specialOfferLiveData.observe(viewLifecycleOwner) {
            specialOfferItemsList.clear()
            specialOfferItemsList.addAll(it)
            binding.specialOfferRecyclerview.adapter?.notifyDataSetChanged()
        }
    }
}




class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}