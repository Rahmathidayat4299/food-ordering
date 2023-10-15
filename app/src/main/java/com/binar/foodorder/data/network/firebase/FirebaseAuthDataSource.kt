package com.binar.foodorder.data.network.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.math.log


interface FirebaseAuthDataSource {

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(email: String, password: String): Boolean

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(fullName: String, email: String, password: String): Boolean
    suspend fun updatePassword(newPassword: String): Boolean
    suspend fun updateProfile(
        fullName: String? = null,
        email: Uri? = null,
    ): Boolean

    fun sendChangePasswordRequestByEmail(): Boolean
    fun doLogut(): Boolean
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): FirebaseUser?
    suspend fun updateEmail(newEmail: String): Boolean

}

class FirebaseAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthDataSource {

    @Throws(exceptionClasses = [Exception::class])
    override suspend fun doLogin(email: String, password: String): Boolean {
        val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return loginResult != null
    }

    @Throws(exceptionClasses = [Exception::class])
    override suspend fun doRegister(fullName: String, email: String, password: String): Boolean {
        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = fullName
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun updatePassword(newPassword: String): Boolean {
        getCurrentUser()?.updatePassword(newPassword)?.await()
        return true
    }

    override suspend fun updateProfile(fullName: String?, email: Uri?): Boolean {
        getCurrentUser()?.updateProfile(
            userProfileChangeRequest {
                fullName?.let { displayName = fullName }
                photoUri?.let { this.photoUri = it }
            }
        )?.await()
        return true
    }

    override fun sendChangePasswordRequestByEmail(): Boolean {
        getCurrentUser()?.email?.let { firebaseAuth.sendPasswordResetEmail(it) }
        return true
    }

    override fun doLogut(): Boolean {
        Firebase.auth.signOut()
        return true
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun updateEmail(newEmail: String): Boolean {
        getCurrentUser()?.updateEmail(newEmail)?.await()
        return true
    }

}