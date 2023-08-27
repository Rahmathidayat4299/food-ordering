package com.binar.foodorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binar.foodorder.data.FoodLocalDataSource
import com.binar.foodorder.repository.FoodRepository


class MainActivity : AppCompatActivity() {
    private lateinit var foodViewModel:FoodViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val foodDataSource = FoodLocalDataSource()
        val foodRepository = FoodRepository(foodDataSource)
        val viewModelFactory = FoodViewModelFactory(foodRepository)
        foodViewModel = ViewModelProvider(this, viewModelFactory)[FoodViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recycleviewFood)
        val adapter = FoodAdapter(foodViewModel.foods.value ?: emptyList())

        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter

        foodViewModel.foods.observe(this) { foods ->
            adapter.notifyDataSetChanged()
        }
    }
}