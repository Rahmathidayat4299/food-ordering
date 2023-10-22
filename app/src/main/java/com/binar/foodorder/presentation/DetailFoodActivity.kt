package com.binar.foodorder.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.binar.foodorder.R
import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.datasource.CartDatabaseDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodService
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.CartRepositoryImpl
import com.binar.foodorder.databinding.ActivityDetailFoodBinding
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import com.binar.foodorder.util.toCurrencyFormatInt
import com.binar.foodorder.viewmodel.DetailViewModel
import com.google.firebase.auth.FirebaseAuth

class DetailFoodActivity : AppCompatActivity() {
    private val binding: ActivityDetailFoodBinding by lazy {
        ActivityDetailFoodBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val service: FoodService by lazy {
            FoodService.invoke()
        }
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val foodNetworkDataSource = FoodNetworkDataSourceImpl(service)
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource,foodNetworkDataSource,firebaseDataSource)
        GenericViewModelFactory.create(DetailViewModel(intent.extras, repo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindFood(viewModel.food)
        observeData()
        setOnClick()

    }

    private fun bindFood(food: Food?) {
        food?.let { itemFood ->
            binding.imageView.load(itemFood.imageUrl)
            binding.tvFoodDetail.text = itemFood.nama
            binding.tvDescription.text = itemFood.detail

        }
    }

    private fun setOnClick() {
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvIncrement.setOnClickListener {
            viewModel.add()
        }
        binding.tvDecrement.setOnClickListener {
            viewModel.minus()
        }
        binding.btnCart.setOnClickListener {
            viewModel.addToCart()
        }

        binding.mapView.setOnClickListener {
            val gmmIntentUri = Uri.parse("https://maps.app.goo.gl/h4wQKqaBuXzftGK77")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            startActivity(mapIntent)
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this) {
            binding.textPriceDetail.text = it.toCurrencyFormat()
            val formatPrice = it.toCurrencyFormat()
            val addToCartText = getString(R.string.add_to_cart, formatPrice)
            binding.btnCart.text = addToCartText
        }
        viewModel.productCountLiveData.observe(this) {
            binding.tvQuantity.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                }, doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                })
        }

    }


    companion object {
        const val EXTRA_FOOD = "EXTRA_FOOD"
        fun startActivity(context: Context, food: Food) {
            val intent = Intent(context, DetailFoodActivity::class.java)
            intent.putExtra(EXTRA_FOOD, food)
            context.startActivity(intent)
        }
    }
}