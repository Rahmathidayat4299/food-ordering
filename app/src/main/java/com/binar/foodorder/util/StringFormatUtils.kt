package com.binar.foodorder.util

import android.icu.text.NumberFormat
import android.icu.util.Currency
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Created by Rahmat Hidayat on 30/09/2023.
 */
fun Double.toCurrencyFormat():String{
    val locale = Locale("id", "ID") // Indonesian locale
    val format: NumberFormat = NumberFormat.getCurrencyInstance(locale)
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IDR")
    return format.format(this.roundToInt()).replace("IDR", "Rp")
}

fun Int.toCurrencyFormatInt(): String {
    val locale = Locale("id", "ID") // Indonesian locale
    val format: NumberFormat = NumberFormat.getCurrencyInstance(locale)
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IDR")
    return format.format(this).replace("IDR", "Rp")
}