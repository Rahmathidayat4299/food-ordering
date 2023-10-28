package com.binar.foodorder.util

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.binar.foodorder.R

fun AppCompatEditText.doneEditing(doneBlock: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
            actionId == EditorInfo.IME_ACTION_DONE ||
            event != null &&
            event.action == KeyEvent.ACTION_DOWN &&
            event.keyCode == KeyEvent.KEYCODE_ENTER
        ) {
            if (event == null || !event.isShiftPressed) {
                // the user is done typing.
                doneBlock.invoke()
                return@setOnEditorActionListener true
            }
        }
        return@setOnEditorActionListener true
    }
}

fun EditText.onTextChangedWithValidation(
    onTextChanged: (String, EditText) -> Unit,
    onValidationError: (String, EditText) -> Unit
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            val text = s.toString()
            val editText = this@onTextChangedWithValidation
            var isValid = true

            // Memeriksa validitas alamat email
            if (editText.id == R.id.emailEt && !emailValid(text)) {
                isValid = false
                onValidationError("Alamat email tidak valid", editText)
            }

            // Memeriksa panjang password login
            if (editText.id == R.id.passET && text.length < 8) {
                isValid = false
                onValidationError("Password harus lebih atau sama dengan 8 karakter", editText)
            }
            if (editText.id == R.id.PassEt && text.length < 8) {
                isValid = false
                onValidationError("Password harus lebih atau sama dengan 8 karakter", editText)
            }

            if (isValid) {
                onTextChanged(text, editText)
            }
        }
    })
}

fun emailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
