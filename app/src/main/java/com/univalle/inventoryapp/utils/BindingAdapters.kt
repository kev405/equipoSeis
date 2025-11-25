package com.univalle.inventoryapp.utils

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:textStyle")
    fun setTextStyle(view: TextView, style: Int) {
        view.setTypeface(null, style)
    }
}