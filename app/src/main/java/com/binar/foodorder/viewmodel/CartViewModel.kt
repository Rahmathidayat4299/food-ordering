package com.binar.foodorder.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.model.Cart
import com.binar.foodorder.util.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    val cartList = repo.getUserCartData().asLiveData(Dispatchers.IO)

    fun decreaseCart(item: Cart) {
        viewModelScope.launch { repo.decreaseCart(item).collect() }
    }
    fun increaseCart(item: Cart) {
        viewModelScope.launch { repo.increaseCart(item).collect() }
    }
    fun removeCart(item: Cart) {
        viewModelScope.launch { repo.deleteCart(item).collect() }
    }
    fun setCartNotes(item: Cart) {
        viewModelScope.launch { repo.setCartNotes(item).collect() }
    }

    private val _confirmationOrder = MutableLiveData<ResultWrapper<Boolean>>()
    val confirmationOrder : LiveData<ResultWrapper<Boolean>>
        get() = _confirmationOrder

    fun createOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            val list =cartList.value?.payload?.first ?:return@launch
            Log.d("ConfirmViewModel", "createOrder:$list ")
            repo.createOrder(list).collect{
                _confirmationOrder.postValue(it)
            }
        }
    }





}