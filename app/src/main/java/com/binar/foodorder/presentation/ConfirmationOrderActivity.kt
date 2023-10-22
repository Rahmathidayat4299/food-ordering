package com.binar.foodorder.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.adapter.CartListAdapter
import com.binar.foodorder.adapter.CartListener
import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.datasource.CartDatabaseDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodService
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.CartRepositoryImpl
import com.binar.foodorder.data.repository.UserRepositoryImpl
import com.binar.foodorder.databinding.ActivityConfirmationOrderBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import com.binar.foodorder.viewmodel.CartViewModel
import com.binar.foodorder.viewmodel.ConfirmationOrderViewModel
import com.binar.foodorder.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth


class ConfirmationOrderActivity : AppCompatActivity() {
    private val binding: ActivityConfirmationOrderBinding by lazy {
        ActivityConfirmationOrderBinding.inflate(layoutInflater)
    }

    private val viewModel: CartViewModel by viewModels {
        val service: FoodService by lazy {
            FoodService.invoke()
        }
        val firebaseAuth = FirebaseAuth.getInstance()
        val foodNetworkDataSource = FoodNetworkDataSourceImpl(service)
        val firebaseDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository =
            CartRepositoryImpl(cartDataSource, foodNetworkDataSource, firebaseDataSource)
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
        setUpRecycleview()
        back()
        observeOrderSuccess()
        setOnclick()

    }

    private fun setOnclick() {

        binding.btnCheckout.setOnClickListener {
            viewModel.createOrder()
        }
    }

    private fun observeOrderSuccess() {
        viewModel.confirmationOrder.observe(this){result->
            result.proceedWhen(
                doOnSuccess = {
                    showDialogCheckoutSuccess()
                    viewModel.clearCart()
                },
                doOnError = {
                    Toast.makeText(this, "Error post ${it.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            )

        }
    }

    private fun showDialogCheckoutSuccess() {
        AlertDialog.Builder(this)
            .setMessage("Checkout Success")
            .setPositiveButton(getString(R.string.text_okay)) { _, _ ->
                finish()
            }.create().show()
    }


    private fun setUpRecycleview() {
        val recyclerView = binding.recycleViewOrder
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter

        viewModel.cartList.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    recyclerView.isVisible = true
                    binding.progresbarOrderConfirmation.isVisible = false
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvPembayaran.text = totalPrice.toCurrencyFormat()
                        Log.d("Testt", "setUpRecycleview:${result.payload.first} ")
                    }
                }, doOnLoading = {
                    binding.progresbarOrderConfirmation.isVisible = true
                    recyclerView.isVisible = false
                }
            )
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun back() {
        binding.icBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }
}