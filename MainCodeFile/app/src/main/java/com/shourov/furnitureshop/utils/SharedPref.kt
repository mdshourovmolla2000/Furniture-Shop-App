package com.shourov.furnitureshop.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPref {
    private var mSharedPref: SharedPreferences? = null

    fun init(context: Context?) {
        if (mSharedPref == null)
            mSharedPref = context!!.getSharedPreferences("com.shourov.furnitureshop", Context.MODE_PRIVATE)
    }

    fun read(key: String?, defValue: String?): String? = mSharedPref?.getString(key, defValue)

    fun write(key: String, value: String) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }
}