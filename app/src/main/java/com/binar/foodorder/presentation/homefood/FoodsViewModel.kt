package com.binar.foodorder.presentation.homefood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.data.repository.FoodsRepository
import com.binar.foodorder.model.Category
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodsViewModel(
    private val repository: FoodsRepository
) : ViewModel() {
    private val _responseFood = MutableLiveData<ResultWrapper<List<Food>>>()
    val responseFood: LiveData<ResultWrapper<List<Food>>>
        get() = _responseFood

    private val _responseCategory = MutableLiveData<ResultWrapper<List<Category>>>()
    val responseCategory: LiveData<ResultWrapper<List<Category>>>
        get() = _responseCategory

    private val _responseCategoryMie = MutableLiveData<ResultWrapper<List<Food>>>()
    val responseCategoryMie: LiveData<ResultWrapper<List<Food>>>
        get() = _responseCategoryMie

    fun getFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFoods().collect {
                _responseFood.postValue(it)
            }
        }
    }

    fun getCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListCategory().collect {
                _responseCategory.postValue(it)
            }
        }
    }

    fun getCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategory(category).collect {
                _responseCategoryMie.postValue(it)
            }
        }
    }
}
