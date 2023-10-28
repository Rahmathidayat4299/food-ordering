package com.binar.foodorder.presentation.detailfood

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import coil.load
import com.binar.foodorder.R
import com.binar.foodorder.databinding.ActivityDetailFoodBinding
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFoodActivity : AppCompatActivity() {
    private val binding: ActivityDetailFoodBinding by lazy {
        ActivityDetailFoodBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
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
                },
                doOnError = { it ->
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            )
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
