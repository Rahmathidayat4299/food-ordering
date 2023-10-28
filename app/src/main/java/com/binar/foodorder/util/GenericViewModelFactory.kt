package com.binar.foodorder.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Rahmat Hidayat on 26/09/2023.
 */
object GenericViewModelFactory {
    fun create(vm: ViewModel) = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = vm as T
    }
}
