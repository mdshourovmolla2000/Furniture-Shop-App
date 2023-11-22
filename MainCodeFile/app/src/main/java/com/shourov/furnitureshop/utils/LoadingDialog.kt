package com.shourov.furnitureshop.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.shourov.furnitureshop.databinding.DialogLoadingLayoutBinding

class LoadingDialog(private val context: Context) {

    private lateinit var loadingAlertDialog: AlertDialog
    private lateinit var binding: DialogLoadingLayoutBinding

    init {
        init()
    }

    private fun init() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        binding = DialogLoadingLayoutBinding.inflate(LayoutInflater.from(context))

        builder.setView(binding.root)
        builder.setCancelable(false)
        loadingAlertDialog = builder.create()

        // make transparent to default dialog
        loadingAlertDialog.window?.setBackgroundDrawable(ColorDrawable(0))
    }

    fun show() = loadingAlertDialog.show()

    fun dismiss() = loadingAlertDialog.dismiss()

    fun setText(text: String?) {
        binding.loadingTextTextview.text = text ?: "Loading"
    }
}
