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
                    binding.bottomNavigationMenuLayout.visibility = View.VISIBLE
                    bottomMenuSelectedColor(binding.bottomNavigationHomeMenuIcon, binding.bottomNavigationHomeMenuTextview)
                }
                R.id.favouriteFragment-> {
                    binding.bottomNavigationMenuLayout.visibility = View.VISIBLE
                    bottomMenuSelectedColor(binding.bottomNavigationFavouriteMenuIcon, binding.bottomNavigationFavouriteMenuTextview)
                }
                R.id.shoppingFragment-> {
                    binding.bottomNavigationMenuLayout.visibility = View.VISIBLE
                    bottomMenuSelectedColor(binding.bottomNavigationShoppingMenuIcon, binding.bottomNavigationShoppingMenuTextview)
                }
                R.id.profileFragment-> {
                    binding.bottomNavigationMenuLayout.visibility = View.VISIBLE
                    bottomMenuSelectedColor(binding.bottomNavigationProfileMenuIcon, binding.bottomNavigationProfileMenuTextview)
                }
                else -> {
                    binding.bottomNavigationMenuLayout.visibility = View.GONE
                }
            }
        }

        binding.bottomNavigationHomeMenu.setOnClickListener {
            if (currentFragmentId != R.id.homeFragment) {
                navController.popBackStack()
            }
        }

        binding.bottomNavigationFavouriteMenu.setOnClickListener {
            if (currentFragmentId == R.id.homeFragment) {
                navController.navigate(R.id.action_homeFragment_to_favouriteFragment)
            } else if(currentFragmentId != R.id.favouriteFragment) {
                navController.popBackStack()
                navController.navigate(R.id.action_homeFragment_to_favouriteFragment)
            }
        }

        binding.bottomNavigationShoppingMenu.setOnClickListener {
            if (currentFragmentId == R.id.homeFragment) {
                navController.navigate(R.id.action_homeFragment_to_shoppingFragment)
            } else if(currentFragmentId != R.id.shoppingFragment) {
                navController.popBackStack()
                navController.navigate(R.id.action_homeFragment_to_shoppingFragment)
            }
        }

        binding.bottomNavigationProfileMenu.setOnClickListener {
            if (currentFragmentId == R.id.homeFragment) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment)
            } else if(currentFragmentId != R.id.profileFragment) {
                navController.popBackStack()
                navController.navigate(R.id.action_homeFragment_to_profileFragment)
            }
        }
    }

    private fun bottomMenuSelectedColor(icon: ImageView, text: TextView) {
        binding.bottomNavigationHomeMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
        binding.bottomNavigationFavouriteMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
        binding.bottomNavigationShoppingMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)
        binding.bottomNavigationProfileMenuIcon.setColorFilter(Color.parseColor("#828A89"), PorterDuff.Mode.SRC_IN)

        binding.bottomNavigationHomeMenuTextview.setTextColor(Color.parseColor("#828A89"))
        binding.bottomNavigationFavouriteMenuTextview.setTextColor(Color.parseColor("#828A89"))
        binding.bottomNavigationShoppingMenuTextview.setTextColor(Color.parseColor("#828A89"))
        binding.bottomNavigationProfileMenuTextview.setTextColor(Color.parseColor("#828A89"))

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

            dialogBinding.noButton.setOnClickListener { alertDialog.dismiss() }

            dialogBinding.yesButton.setOnClickListener {
                alertDialog.dismiss()
                finish()
            }

            alertDialog.show()
        } else {
            navController.popBackStack()
        }
    }
}