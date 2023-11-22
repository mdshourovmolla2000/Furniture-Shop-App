package com.shourov.furnitureshop.view.authActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.utils.SharedPref

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        SharedPref.init(this)
    }
}