package com.binar.foodorder.presentation.confirmationorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.adapter.CartListAdapter
import com.binar.foodorder.adapter.CartListener
import com.binar.foodorder.databinding.ActivityConfirmationOrderBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.presentation.cart.CartViewModel
import com.binar.foodorder.presentation.main.MainActivity
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmationOrderActivity : AppCompatActivity() {
    private val binding: ActivityConfirmationOrderBinding by lazy {
        ActivityConfirmationOrderBinding.inflate(layoutInflater)
    }
    private val viewModel: CartViewModel by viewModel()

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
        viewModel.confirmationOrder.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    showDialogCheckoutSuccess()
                    viewModel.clearCart()
                },
                doOnError = {
                    Toast.makeText(this, "Error post ${it.message}", Toast.LENGTH_SHORT).show()
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
                },
                doOnLoading = {
                    binding.progresbarOrderConfirmation.isVisible = true
                    recyclerView.isVisible = false
                }
            )
        }
    }

    private fun back() {
        binding.icBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}
