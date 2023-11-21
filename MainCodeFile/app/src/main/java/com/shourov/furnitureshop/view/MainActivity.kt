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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.databinding.ActivityMainBinding
import com.shourov.furnitureshop.databinding.DialogExitBinding

class MainActivity : AppCompatActivity() {

    //Declaring variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentFragmentId = R.id.homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

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
                R.id.shoppingFragment-> {
                    binding.apply {
                        bottomNavigationMenuLayout.visibility = View.VISIBLE
                        bottomMenuSelectedColor(bottomNavigationShoppingMenuIcon, bottomNavigationShoppingMenuTextview)
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

            bottomNavigationShoppingMenu.setOnClickListener {
                if (currentFragmentId == R.id.homeFragment) {
                    navController.navigate(R.id.action_homeFragment_to_shoppingFragment)
                } else if(currentFragmentId != R.id.shoppingFragment) {
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

    private fun bottomMenuSelectedColor(icon: ImageView, text: TextView) {
        binding.apply {
            bottomNavigationHomeMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            bottomNavigationFavouriteMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            bottomNavigationShoppingMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
            bottomNavigationProfileMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)

            bottomNavigationHomeMenuTextview.setTextColor(Color.parseColor("#828A89"))
            bottomNavigationFavouriteMenuTextview.setTextColor(Color.parseColor("#828A89"))
            bottomNavigationShoppingMenuTextview.setTextColor(Color.parseColor("#828A89"))
            bottomNavigationProfileMenuTextview.setTextColor(Color.parseColor("#828A89"))
        }

        icon.setColorFilter(Color.parseColor("#0C8A7B"), PorterDuff.Mode.SRC_IN)
        text.setTextColor(Color.parseColor("#0C8A7B"))
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