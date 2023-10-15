package com.binar.foodorder.viewmodel

import androidx.lifecycle.ViewModel
import com.binar.foodorder.data.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repository.isLoggedIn()
}