package com.sss.core.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    private val usdFormatter = ThreadLocal.withInitial {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            currency = Currency.getInstance("USD")
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    fun formatUsd(amount: BigDecimal): String = usdFormatter.get()!!.format(amount)

    fun formatUsd(amount: Double): String = usdFormatter.get()!!.format(amount)
}
