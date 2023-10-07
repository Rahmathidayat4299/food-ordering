package com.binar.foodorder.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.binar.foodorder.databinding.ItemFoodListviewBinding
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.toCurrencyFormat

/**
 * Created by Rahmat Hidayat on 10/09/2023.
 */
class ListViewHolder(
    private val itemListView: ItemFoodListviewBinding,
    private val onItemClick: (Food) -> Unit
) :
    RecyclerView.ViewHolder(itemListView.root) {

    fun bindFullWidth(food: Food) {
        itemListView.tvFoodname.text = food.name
        itemListView.tvFoodprice.text = food.Price.toCurrencyFormat()
        itemListView.tvFoodimage.load(food.Image)
        itemListView.root.setOnClickListener { onItemClick.invoke(food) }
    }

}