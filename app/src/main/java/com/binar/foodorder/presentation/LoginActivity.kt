package com.binar.foodorder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.binar.foodorder.R
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.repository.UserRepositoryImpl
import com.binar.foodorder.databinding.ActivityLoginBinding
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.emailValid
import com.binar.foodorder.util.highLightWord
import com.binar.foodorder.util.onTextChangedWithValidation
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): LoginViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return LoginViewModel(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        editTextFilled()
        setClickListener()
        observerResult()
    }

    private fun observerResult() {
        viewModel.loginResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    Toast.makeText(
                        this,
                        "Login Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnLogin.isVisible = false
                }
            )
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }


    private fun setClickListener() {

        binding.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.tvNavigateRegister.highLightWord(getString(R.string.text_highlight_register)) {
            navigateToRegister()
        }
    }

    private fun doLogin() {
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passET.text.toString().trim()
        viewModel.doLogin(
            email, password
        )
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun editTextFilled() {
        binding.emailEt.onTextChangedWithValidation(
            onTextChanged = { email, editText ->
                enableEditText()
            },
            onValidationError = { errorMessage, editText ->
                editText.error = errorMessage
            }
        )

        binding.passET.onTextChangedWithValidation(
            onTextChanged = { password, editText ->
                enableEditText()
            },
            onValidationError = { errorMessage, editText ->
                editText.error = errorMessage
            }
        )
    }

    private fun enableEditText() {
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passET.text.toString().trim()
        binding.btnLogin.isEnabled =
            email.isNotEmpty() && emailValid(email) && password.length >= 8 && password.isNotEmpty()
    }

}