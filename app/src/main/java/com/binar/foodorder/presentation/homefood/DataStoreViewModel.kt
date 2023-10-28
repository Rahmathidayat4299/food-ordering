package com.binar.foodorder.presentation.homefood

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.data.local.datastore.ViewDataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatastoreViewModel(private val pref: ViewDataStoreManager) : ViewModel() {

    val vIsLinearView: LiveData<Boolean> =
        pref.getIsLinearView().asLiveData(Dispatchers.IO) // Menambah LiveData untuk isLinearView

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