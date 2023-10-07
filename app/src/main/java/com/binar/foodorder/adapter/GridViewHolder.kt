package com.binar.foodorder.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.binar.foodorder.databinding.ItemFoodGridviewBinding
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.toCurrencyFormat

/**
 * Created by Rahmat Hidayat on 10/09/2023.
 */
class GridViewHolder(
    private val binding: ItemFoodGridviewBinding,
    private val onItemClick: (Food) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindDefault(food: Food) {
        binding.tvFoodname.text = food.name
        binding.tvFoodprice.text = food.Price.toCurrencyFormat()
        binding.tvFoodimage.load(food.Image)
        binding.root.setOnClickListener {
            onItemClick.invoke(food)
        }
    }


}