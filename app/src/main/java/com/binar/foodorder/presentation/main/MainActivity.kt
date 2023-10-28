package com.binar.foodorder.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigation()
        observeLinearView()
    }
    private fun observeLinearView() {
        viewModel.isLinearView.observe(this) { isLinearView ->
            if (isLinearView) {
                LinearLayoutManager(this)
            } else {
                GridLayoutManager(this, 2)
            }
        }
    }

    private fun setUpBottomNavigation() {
        val bottomNavController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationFood.setupWithNavController(bottomNavController)
    }
}
