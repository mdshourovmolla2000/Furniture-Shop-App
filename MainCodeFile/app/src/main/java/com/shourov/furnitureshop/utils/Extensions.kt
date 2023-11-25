package com.shourov.furnitureshop.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shourov.furnitureshop.R
import es.dmoral.toasty.Toasty

fun ImageView.loadImage(resource: String?, placeholder: Int = R.drawable.loading_image) = Glide.with(this.context).load(resource).placeholder(placeholder).error(placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)

fun ImageView.loadImage(resource: Int?, placeholder: Int = R.drawable.loading_image) = Glide.with(this.context).load(resource).placeholder(placeholder).error(placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)

fun ImageView.loadImage(resource: Uri?, placeholder: Int = R.drawable.loading_image) = Glide.with(this.context).load(resource).placeholder(placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).error(placeholder).skipMemoryCache(true).into(this)

fun Context.showSuccessToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toasty.success(this, message, duration, true).show()

fun Context.showErrorToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toasty.error(this, message, duration, true).show()

fun Context.showWarningToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toasty.warning(this, message, duration, true).show()

fun Context.showInfoToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toasty.info(this, message, duration, true).show()