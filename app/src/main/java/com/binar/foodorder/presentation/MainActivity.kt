package com.binar.foodorder.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.databinding.ActivityMainBinding
import com.binar.foodorder.data.local.datastore.ViewDataStoreManager
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        val vds: ViewDataStoreManager = ViewDataStoreManager(this)
        GenericViewModelFactory.create(MainViewModel(vds))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        throw IllegalArgumentException("test crash")
        setUpBottomNavigation()
        observeLinearView()
    }

    private fun observeLinearView() {
        viewModel.isLinearView.observe(this){isLinearView->
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