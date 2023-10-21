package com.binar.foodorder.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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
import com.binar.foodorder.data.network.api.CategoryNetworkDataSource
import com.binar.foodorder.data.network.api.CategoryNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodService
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.CartRepositoryImpl
import com.binar.foodorder.data.repository.FoodsRepository
import com.binar.foodorder.data.repository.FoodsRepositoryImpl
import com.binar.foodorder.data.repository.UserRepositoryImpl
import com.binar.foodorder.databinding.ActivityConfirmationOrderBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import com.binar.foodorder.viewmodel.CartViewModel
import com.binar.foodorder.viewmodel.ConfirmationOrderViewModel
import com.binar.foodorder.viewmodel.FoodsViewModel
import com.binar.foodorder.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth


class ConfirmationOrderActivity : AppCompatActivity() {
    private val binding: ActivityConfirmationOrderBinding by lazy {
        ActivityConfirmationOrderBinding.inflate(layoutInflater)
    }

    private val viewModelProfile: ProfileViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }
    private val foodsViewModelApi: ConfirmationOrderViewModel by viewModels {
        val service: FoodService by lazy {
            FoodService.invoke()
        }
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource = CartDatabaseDataSource(cartDao)
        val dataUser = FirebaseAuthDataSourceImpl(firebaseAuth)
        val nds: FoodNetworkDataSource = FoodNetworkDataSourceImpl(service)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource, nds, dataUser)
        GenericViewModelFactory.create(ConfirmationOrderViewModel(repo))
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
        setOnclick()
        observeOrderSuccess()
    }

    private fun setOnclick() {

        binding.btnCheckout.setOnClickListener {
            foodsViewModelApi.createOrder()
//            Toast.makeText(this, "order succes", Toast.LENGTH_SHORT).show()
            Log.d("Post Request", "setOnclick: ${foodsViewModelApi.createOrder()}")


        }
    }

    private fun observeOrderSuccess() {
        foodsViewModelApi.confirmationOrder.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    showDialogCheckoutSuccess()
                    Toast.makeText(this, "order succes", Toast.LENGTH_SHORT).show()

                    Log.d("Post Request Sukses", "setOnclick: ${foodsViewModelApi.createOrder()}")
                },
                doOnError = {
                    Toast.makeText(this, "order gagal", Toast.LENGTH_SHORT).show()
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

    //    private fun showDialog(){
//
//        binding.btnCheckout.setOnClickListener {
//
//            foodsViewModelApi.createOrder()
//            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
//                .create()
//            val view = layoutInflater.inflate(R.layout.dialog_view,null)
//            val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
//            builder.setView(view)
//            button.setOnClickListener {
//                builder.dismiss()
//                navigateToHome()
//            }
//            builder.setCanceledOnTouchOutside(false)
//            builder.show()
//        }
//    }
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