package com.binar.foodorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.binar.foodorder.data.repository.FoodRepository
import com.binar.foodorder.model.Category
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.ResultWrapper
import kotlinx.coroutines.Dispatchers

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
class FoodViewModel(private val foodRepository: FoodRepository) : ViewModel() {
    val foods: LiveData<ResultWrapper<List<Food>>> =
        foodRepository.getFoods().asLiveData(Dispatchers.IO)

    val category: LiveData<List<Category>> = liveData(Dispatchers.IO) {
        val categories = foodRepository.getCategories()
        emit(categories)
    }
}
