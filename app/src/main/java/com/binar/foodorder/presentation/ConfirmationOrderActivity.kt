package com.binar.foodorder.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.binar.foodorder.R
import com.binar.foodorder.databinding.ActivityConfirmationOrderBinding


class ConfirmationOrderActivity : AppCompatActivity() {
    private val binding: ActivityConfirmationOrderBinding by lazy {
        ActivityConfirmationOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showDialog()
    }
    private fun showDialog(){
        binding.btnCheckout.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.dialog_view,null)
            val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
                navigateToHome()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }
    }
    private fun navigateToHome(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}