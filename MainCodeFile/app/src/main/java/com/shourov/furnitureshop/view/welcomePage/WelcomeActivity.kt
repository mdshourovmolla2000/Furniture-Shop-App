package com.shourov.furnitureshop.view.welcomePage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.utils.SharedPref

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        SharedPref.init(this)
    }
}