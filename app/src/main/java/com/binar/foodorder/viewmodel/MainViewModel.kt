package com.binar.foodorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.repository.ViewDataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Rahmat Hidayat on 30/09/2023.
 */
class MainViewModel(private val pref: ViewDataStoreManager) : ViewModel() {
    val isLinearView = pref.getIsLinearView().asLiveData(Dispatchers.IO)
    val vIsLinearView: LiveData<Boolean> = pref.getIsLinearView().asLiveData(Dispatchers.IO) // Menambah LiveData untuk isLinearView

    // Menambahkan fungsi untuk mengatur isLinearView
    fun setIsLinearView(isLinear: Boolean) {
        viewModelScope.launch {
            pref.setIsLinearView(isLinear)
        }
    }

    // Menambahkan fungsi untuk mendapatkan isLinearView
    fun getIsLinearView(): LiveData<Boolean> {
        return vIsLinearView
    }
}