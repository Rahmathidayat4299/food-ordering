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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
class FoodViewModel(private val foodRepository: FoodRepository) : ViewModel() {
//    val foods: LiveData<List<Food>>
//        get() = foodRepository.getFoods().map {
//            it.payload ?: emptyList()
//        }.asLiveData(Dispatchers.IO)
val foods: LiveData<ResultWrapper<List<Food>>> = foodRepository.getFoods().asLiveData(Dispatchers.IO)
    val category: LiveData<List<Category>> = liveData(Dispatchers.IO) {
        val categories = foodRepository.getCategories()
        emit(categories)
    }
//    private val _food = MutableLiveData<List<Food>>()
//    val foods: LiveData<List<Food>> get() = _food
//
//    private val _category = MutableLiveData<List<Category>>()
//    val category: LiveData<List<Category>> get() = _category
//
//    init {
////        _food.value = viewModelScope.launch(Dispatchers.IO) { foodRepository.getFood() }
//        _category.value = foodRepository.getCategory()
//        viewModelScope.launch(Dispatchers.IO) {
//            val foodList = foodRepository.getFood()
//            _food.postValue(foodList)
//        }
//    }


}
