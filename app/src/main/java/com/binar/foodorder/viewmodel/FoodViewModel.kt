package com.binar.foodorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.model.Category
import com.binar.foodorder.model.Food
import com.binar.foodorder.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
class FoodViewModel(private val foodRepository: FoodRepository) : ViewModel() {
    private val _food = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> get() = _food

    private val _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>> get() = _category

    init {
//        _food.value = viewModelScope.launch(Dispatchers.IO) { foodRepository.getFood() }
        _category.value = foodRepository.getCategory()
        viewModelScope.launch {
            val foodList = foodRepository.getFood()
            _food.postValue(foodList)
        }
    }




}
