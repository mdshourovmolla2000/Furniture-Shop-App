package com.shourov.furnitureshop.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shourov.furnitureshop.R

fun ImageView.loadImage(resource: Int?, placeholder: Int = R.drawable.image_placeholder_square) = Glide.with(this.context).load(resource).placeholder(placeholder).error(placeholder).into(this)