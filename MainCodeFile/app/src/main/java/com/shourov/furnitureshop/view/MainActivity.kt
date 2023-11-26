package com.shourov.furnitureshop.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.ActivityMainBinding
import com.shourov.furnitureshop.databinding.DialogExitBinding
import com.shourov.furnitureshop.repository.MainRepository
import com.shourov.furnitureshop.utils.LoadingDialog
import com.shourov.furnitureshop.utils.SharedPref
import com.shourov.furnitureshop.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    //Declaring variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentFragmentId = R.id.homeFragment

    lateinit var viewModel: MainViewModel

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPref.init(this)
        loadingDialog = LoadingDialog(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        viewModel = ViewModelProvider(this, MainViewModelFactory(MainRepository()))[MainViewModel::class.java]

        observerList()

        //this will run every time when any fragment changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragmentId = destination.id
            when (currentFragmentId) {
                R.id.homeFragment-> {
                    binding.apply {
                        bottomNavigationMenuLayout.visibility = View.VISIBLE
                        bottomMenuSelectedColor(bottomNavigationHomeMenuIcon, bottomNavigationHomeMenuTextview)
                    }
                }
                R.id.favouriteFragment-> {
                    binding.apply {
                        bottomNavigationMenuLayout.visibility = View.VISIBLE
                        bottomMenuSelectedColor(bottomNavigationFavouriteMenuIcon, bottomNavigationFavouriteMenuTextview)
                    }
                }
                R.id.cartFragment-> {
                    binding.apply {
                        bottomNavigationMenuLayout.visibility = View.VISIBLE
                        bottomMenuSelectedColor(bottomNavigationCartMenuIcon, bottomNavigationCartMenuTextview)
                    }
                }
                R.id.profileFragment-> {
                    binding.apply {
                        bottomNavigationMenuLayout.visibility = View.VISIBLE
                        bottomMenuSelectedColor(bottomNavigationProfileMenuIcon, bottomNavigationProfileMenuTextview)
                    }
                }
                else -> {
                    binding.bottomNavigationMenuLayout.visibility = View.GONE
                }
            }
        }

        binding.apply {
            bottomNavigationHomeMenu.setOnClickListener {
                if (currentFragmentId != R.id.homeFragment) { navController.popBackStack() }
            }

            bottomNavigationFavouriteMenu.setOnClickListener {
                if (currentFragmentId == R.id.homeFragment) {
                    navController.navigate(R.id.action_homeFragment_to_favouriteFragment)
                } else if(currentFragmentId != R.id.favouriteFragment) {
                    navController.popBackStack()
                    navController.navigate(R.id.action_homeFragment_to_favouriteFragment)
                }
            }

            bottomNavigationCartMenu.setOnClickListener {
                if (currentFragmentId == R.id.homeFragment) {
                    navController.navigate(R.id.action_homeFragment_to_shoppingFragment)
                } else if(currentFragmentId != R.id.cartFragment) {
                    navController.popBackStack()
                    navController.navigate(R.id.action_homeFragment_to_shoppingFragment)
                }
            }

            bottomNavigationProfileMenu.setOnClickListener {
                if (currentFragmentId == R.id.homeFragment) {
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                } else if(currentFragmentId != R.id.profileFragment) {
                    navController.popBackStack()
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                }
            }
        }
    }

    private fun observerList() {
        viewModel.loadingDialogLiveData.observe(this) { it?.let { isLoading -> if (isLoading) loadingDialog.show() else loadingDialog.dismiss() } }

        viewModel.loadingDialogTextLiveData.observe(this) { loadingDialog.setText(it) }
    }

    private fun bottomMenuSelectedColor(icon: ImageView, text: TextView) {
        binding.apply {
            bottomNavigationHomeMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            bottomNavigationFavouriteMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            bottomNavigationCartMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            bottomNavigationProfileMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)

            bottomNavigationHomeMenuTextview.setTextColor(Color.parseColor("#828A89"))
            bottomNavigationFavouriteMenuTextview.setTextColor(Color.parseColor("#828A89"))
            bottomNavigationCartMenuTextview.setTextColor(Color.parseColor("#828A89"))
            bottomNavigationProfileMenuTextview.setTextColor(Color.parseColor("#828A89"))
        }

        icon.setColorFilter(ContextCompat.getColor(this, R.color.themeColor), PorterDuff.Mode.SRC_IN)
        text.setTextColor(ContextCompat.getColor(this, R.color.themeColor))
    }

    override fun onBackPressed() {
        if (currentFragmentId == R.id.homeFragment) {
            val builder = AlertDialog.Builder(this)
            val dialogBinding = DialogExitBinding.inflate(layoutInflater)

            builder.setView(dialogBinding.root)
            builder.setCancelable(true)

            val alertDialog = builder.create()

            //make transparent to default dialog
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

            dialogBinding.apply {
                noButton.setOnClickListener { alertDialog.dismiss() }
                yesButton.setOnClickListener {
                    alertDialog.dismiss()
                    finish()
                }
            }

            alertDialog.show()
        } else {
            super.onBackPressed()
        }
    }
}






class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(repository) as T
}