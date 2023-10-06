package com.binar.foodorder.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.adapter.CategoryAdapter
import com.binar.foodorder.adapter.FoodAdapter
import com.binar.foodorder.data.dummy.DummyCategoryDataSource
import com.binar.foodorder.data.dummy.DummyCategoryDataSourceImpl
import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.local.database.datasource.FoodDatabaseDataSource
import com.binar.foodorder.data.local.datastore.ViewDataStoreManager
import com.binar.foodorder.data.repository.FoodRepository
import com.binar.foodorder.data.repository.FoodRepositoryImpl
import com.binar.foodorder.databinding.FragmentHomeFoodBinding
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import com.binar.foodorder.viewmodel.DatastoreViewModel
import com.binar.foodorder.viewmodel.FoodViewModel
import com.binar.foodorder.viewmodel.MainViewModel

class HomeFood : Fragment() {

    private lateinit var binding: FragmentHomeFoodBinding
    private val foodsViewModel: FoodViewModel by viewModels {
     val databaseDataSource = AppDatabase.getInstance(requireContext())
        val categoryDataSource: DummyCategoryDataSource = DummyCategoryDataSourceImpl()
        val foodDao = databaseDataSource.foodDao()
        val foodDataSource = FoodDatabaseDataSource(foodDao)
        val foodRepository: FoodRepository = FoodRepositoryImpl(foodDataSource, categoryDataSource)
        GenericViewModelFactory.create(FoodViewModel(foodRepository))
    }
    private val viewDataStoreViewModel: DatastoreViewModel by viewModels {
        val vds: ViewDataStoreManager = ViewDataStoreManager(requireContext())
        GenericViewModelFactory.create(DatastoreViewModel(vds))
    }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleview()
        setUpRecycleviewCategory()
        Log.d(requireContext().toString(), "Category:${setUpRecycleviewCategory()} ")
        setViewList()
//        setViewListActivity()
    }

    private fun setUpRecycleview() {
        val recyclerView = binding.recycleviewFood
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FoodAdapter(
            onItemClick = { food ->
                navigateToDetail(food)
            },

            viewModel = viewDataStoreViewModel
        )
        recyclerView.adapter = adapter

//        foodsViewModel.foods.observe(viewLifecycleOwner) { foods ->
//            adapter.setData(foods)
//        }
        foodsViewModel.foods.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progresbarHome.isVisible = false
                    binding.recycleviewFood.isVisible = true
                    result.payload?.let { foods ->
                        adapter.setData(foods)
                    }

                }, doOnLoading = {
                    binding.recycleviewFood.isVisible = false
                    binding.progresbarHome.isVisible = true

                }
            )
        }
    }

    private fun setUpRecycleviewCategory() {
        val recyclerView = binding.recyclerviewCategory
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryAdapter(
            onItemClick = { category ->
            },

            )
        recyclerView.adapter = adapter
        foodsViewModel.category.observe(viewLifecycleOwner) { category ->
            adapter.submitListCategory(category)
        }
    }

    private fun navigateToDetailFood(food: Food? = null) {
        val action = HomeFoodDirections.actionHomeFoodToDetailFood(food)
        findNavController().navigate(action)
    }

    //detail to activity
    private fun navigateToDetail(item: Food) {
        DetailFoodActivity.startActivity(requireContext(), item)
    }

    private fun setViewListActivity() {

        mainViewModel.isLinearView.observe(this) { isLinearView ->
            binding.iconList.setOnClickListener {
                viewDataStoreViewModel.setIsLinearView(!isLinearView)
            }
        }

    }

    private fun setViewList() {
        viewDataStoreViewModel.getIsLinearView().observe(viewLifecycleOwner) { isLinearView ->
            toggleRecyclerViewLayout(isLinearView)
            setUpListToggle(isLinearView)
        }
    }

    private fun setUpListToggle(isLinearView: Boolean) {
        val iconList = binding.iconList
        iconList.setImageResource(if (isLinearView) R.drawable.baseline_list_24 else R.drawable.icon_grid)
        iconList.setOnClickListener {
            mainViewModel.setIsLinearView(!isLinearView)
        }
    }

    private fun toggleRecyclerViewLayout(isLinearView: Boolean) {
        val recyclerView = binding.recycleviewFood
        recyclerView.layoutManager = if (isLinearView) {
            LinearLayoutManager(requireContext())

        } else {
            GridLayoutManager(requireContext(), 2)
        }
    }
}
