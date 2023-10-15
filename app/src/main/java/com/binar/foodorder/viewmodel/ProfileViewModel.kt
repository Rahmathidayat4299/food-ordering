package com.binar.foodorder.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binar.foodorder.data.repository.UserRepository
import com.binar.foodorder.util.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _changePhotoResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changePhotoResult: LiveData<ResultWrapper<Boolean>>
        get() = _changePhotoResult

    private val _changeProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changeProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _changeProfileResult

    fun getCurrentUser() = repository.getCurrentUser()

    fun updateProfilePicture(photoUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            photoUri?.let {
                repository.updateProfile(photoUri = photoUri).collect {
                    _changePhotoResult.postValue(it)
                }
            }
        }
    }

    fun updateFullName(fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(fullName = fullName).collect {
                _changeProfileResult.postValue(it)
            }
        }
    }

    fun createChangePwdRequest() {
        repository.sendChangePasswordRequestByEmail()
    }

    fun doLogOut() = repository.doLogout()
}