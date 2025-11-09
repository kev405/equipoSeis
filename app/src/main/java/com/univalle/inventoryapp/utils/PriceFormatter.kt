package com.univalle.inventoryapp.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PriceFormatter {
    private val formatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.GERMAN))

    fun formatPrice(price: BigDecimal): String {
        return formatter.format(price)
    }

    fun formatPrice(price: Int): String {
        return formatter.format(price)
    }

    fun formatPrice(price: Double): String {
        return formatter.format(price)
    }
}