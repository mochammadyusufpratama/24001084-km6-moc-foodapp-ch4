package com.app.makanku.utils

import java.text.NumberFormat
import java.util.Locale

fun Double?.currencyFormat(language: String, country: String): String {
    return try {
        val localeID = Locale(language, country)
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.format(this).toString()
    } catch (err: Exception) {
        null
    }.toString()
}

fun Double?.indonesianCurrency() = this.currencyFormat("in", "ID")