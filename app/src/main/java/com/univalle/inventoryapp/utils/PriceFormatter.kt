package com.univalle.inventoryapp.utils
import java.math.BigDecimal

import java.text.NumberFormat

object PriceFormatter {
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
        isGroupingUsed = true
    }

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