package com.binar.foodorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.network.model.OrderResponse
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.FoodsRepository
import com.binar.foodorder.data.repository.FoodsRepositoryImpl
import com.binar.foodorder.util.ResultWrapper
import com.binar.foodorder.util.proceedFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmationOrderViewModel(
    private val cartRepository: CartRepository
): ViewModel() {
    val cartList = cartRepository.getUserCartData().asLiveData(Dispatchers.IO)
    private val _confirmationOrder = MutableLiveData<ResultWrapper<Boolean>>()
    val confirmationOrder:LiveData<ResultWrapper<Boolean>>
        get() = _confirmationOrder

    fun createOrder(){
        val cartList = cartList.value?.payload?.first?: return
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.createOrder(cartList)
        }
    }
}