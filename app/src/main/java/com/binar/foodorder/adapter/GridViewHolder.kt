package com.binar.foodorder.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.binar.foodorder.databinding.ItemFoodGridviewBinding
import com.binar.foodorder.model.Food

/**
 * Created by Rahmat Hidayat on 10/09/2023.
 */
class GridViewHolder(
    private val binding: ItemFoodGridviewBinding,
    private val onItemClick: (Food) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindDefault(food: Food) {
        binding.tvFoodname.text = food.nama
        binding.tvFoodprice.text = food.hargaFormat
        binding.tvFoodimage.load(food.imageUrl)
        binding.root.setOnClickListener {
            onItemClick.invoke(food)
        }
    }
}
