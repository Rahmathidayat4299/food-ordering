package com.binar.foodorder.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.adapter.CartListAdapter
import com.binar.foodorder.adapter.CartListener
import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.datasource.CartDatabaseDataSource
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.CartRepositoryImpl
import com.binar.foodorder.databinding.ActivityConfirmationOrderBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import com.binar.foodorder.viewmodel.CartViewModel


class ConfirmationOrderActivity : AppCompatActivity() {
    private val binding: ActivityConfirmationOrderBinding by lazy {
        ActivityConfirmationOrderBinding.inflate(layoutInflater)
    }

    private val viewModel: CartViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource)
        GenericViewModelFactory.create(CartViewModel(repo))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {

            }

            override fun onMinusTotalItemCartClicked(cart: Cart) {

            }

            override fun onRemoveCartClicked(cart: Cart) {

            }

            override fun onUserDoneEditingNotes(cart: Cart) {

            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showDialog()
        setUpRecycleview()
        back()
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
    private fun setUpRecycleview() {
        val recyclerView = binding.recycleViewOrder
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter

//        viewModel.cartList.observe(this) { result ->
//            result.payload?.let { (carts, totalPrice) ->
//                adapter.submitData(carts)
//                binding.tvPembayaran.text = totalPrice.toCurrencyFormat()
//            }
//        }
        viewModel.cartList.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess ={
                    recyclerView.isVisible = true
                    binding.progresbarOrderConfirmation.isVisible = false
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvPembayaran.text = totalPrice.toCurrencyFormat()
                    }
                }, doOnLoading = {
                    binding.progresbarOrderConfirmation.isVisible = true
                    recyclerView.isVisible = false
                }
            )
        }
    }
    private fun navigateToHome(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    private fun back(){
        binding.icBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}