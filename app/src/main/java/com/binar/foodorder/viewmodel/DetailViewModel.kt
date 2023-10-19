package com.binar.foodorder.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.model.Food
import com.binar.foodorder.presentation.DetailFoodActivity
import com.binar.foodorder.util.ResultWrapper
import kotlinx.coroutines.launch

/**
 * Created by Rahmat Hidayat on 30/09/2023.
 */
class DetailViewModel(
    private val extras: Bundle?,
    private val cartRepository: CartRepository
) : ViewModel() {

    val food = extras?.getParcelable<Food>(DetailFoodActivity.EXTRA_FOOD)

    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }
    val productCountLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }
    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult: LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult

    fun add() {
        val harga = food?.harga?.toDouble()
        val count = (productCountLiveData.value ?: 0) + 1
        productCountLiveData.postValue(count)
        priceLiveData.postValue(harga?.times(count)?:0.0)
    }

    fun minus() {
val harga = food?.harga?.toDouble()
        if ((productCountLiveData.value ?: 0) > 0) {
            val count = (productCountLiveData.value ?: 0) - 1
            productCountLiveData.postValue(count)
            priceLiveData.postValue(harga?.times(count)?:0.0)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val productQuantity =
                if ((productCountLiveData.value ?: 0) <= 0) 1 else productCountLiveData.value ?: 0
            food?.let {
                cartRepository.createCart(it, productQuantity).collect { result ->
                    _addToCartResult.postValue(result)
                }
            }
        }
    }

}