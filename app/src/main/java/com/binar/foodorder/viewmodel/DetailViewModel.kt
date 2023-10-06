package com.binar.foodorder.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binar.foodorder.model.Food
import com.binar.foodorder.presentation.DetailFoodActivity

/**
 * Created by Rahmat Hidayat on 30/09/2023.
 */
class DetailViewModel(private val extras: Bundle?) : ViewModel() {

    val food = extras?.getParcelable<Food>(DetailFoodActivity.EXTRA_FOOD)

    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }
    val productCountLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }

    fun add() {
        val count = (productCountLiveData.value ?: 0) + 1
        productCountLiveData.postValue(count)
        priceLiveData.postValue(food?.Price?.times(count) ?: 0.0)
    }
    fun minus() {
        if ((productCountLiveData.value ?: 0) > 0) {
            val count = (productCountLiveData.value ?: 0) - 1
            productCountLiveData.postValue(count)
            priceLiveData.postValue(food?.Price?.times(count) ?: 0.0)
        }
    }


//    fun minus() {
//        if((productCountLiveData.value ?: 0) > 0){
//            val count = (productCountLiveData.value ?: 0) -1
//            productCountLiveData.postValue(count)
//            priceLiveData.postValue(food?.Price?.times(count) ?: 0.0)
//        }
//    }


}