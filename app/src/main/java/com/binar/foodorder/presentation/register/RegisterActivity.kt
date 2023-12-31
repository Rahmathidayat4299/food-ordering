package com.binar.foodorder.presentation.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.binar.foodorder.R
import com.binar.foodorder.databinding.ActivityRegisterBinding
import com.binar.foodorder.presentation.login.LoginActivity
import com.binar.foodorder.presentation.main.MainActivity
import com.binar.foodorder.util.emailValid
import com.binar.foodorder.util.highLightWord
import com.binar.foodorder.util.onTextChangedWithValidation
import com.binar.foodorder.util.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        editTextFilled()
        setOnClick()
        observeResult()
    }

    private fun setOnClick() {
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
        binding.tvNavLogin.highLightWord(getString(R.string.text_highlight_login_here)) {
            navigateToLogin()
        }
    }
    private fun observeResult() {
        viewModel.registerResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    navigateToMain()
                    Toast.makeText(
                        this,
                        "Register Success",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    Toast.makeText(
                        this,
                        "Register Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnRegister.isVisible = false
                }
            )
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun doRegister() {
        val email = binding.emailEt.text.toString().trim()
        val fullName = binding.nameEt.text.toString().trim()
        val password = binding.PassEt.text.toString().trim()
        viewModel.doRegister(
            fullName,
            email,
            password
        )
    }
    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        )
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

        binding.PassEt.onTextChangedWithValidation(
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
        val password = binding.PassEt.text.toString().trim()
        binding.btnRegister.isEnabled =
            email.isNotEmpty() && emailValid(email) && password.length >= 8 && password.isNotEmpty()
    }
}
