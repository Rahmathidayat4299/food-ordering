package com.binar.foodorder.util

import android.icu.text.NumberFormat
import android.icu.util.Currency
import kotlin.math.roundToInt

/**
 * Created by Rahmat Hidayat on 30/09/2023.
 */
fun Double.toCurrencyFormat():String{
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IDR")
    return format.format(this.roundToInt())
}