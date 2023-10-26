package com.binar.foodorder.di

import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

object AppInjection {
    val firebaseAuth = FirebaseAuth.getInstance()
    val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
    val repo = UserRepositoryImpl(dataSource)
}