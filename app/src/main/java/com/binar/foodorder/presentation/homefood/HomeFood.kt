package com.binar.foodorder.presentation.homefood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.R
import com.binar.foodorder.adapter.CategoryAdapter
import com.binar.foodorder.adapter.FoodAdapter
import com.binar.foodorder.databinding.FragmentHomeFoodBinding
import com.binar.foodorder.model.Food
import com.binar.foodorder.presentation.detailfood.DetailFoodActivity
import com.binar.foodorder.presentation.main.MainViewModel
import com.binar.foodorder.util.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFood : Fragment() {

    private lateinit var binding: FragmentHomeFoodBinding

    private val foodsViewModelApi: FoodsViewModel by viewModel()
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleviewCategory()
        setUpRecycleview()
        setViewList()
        Log.d(requireContext().toString(), "Category:${setUpRecycleviewCategory()} ")
    }

    private fun viewCategory(category: String) {
        val recyclerView = binding.recycleviewFood
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FoodAdapter(
            onItemClick = { food ->
                navigateToDetail(food)
            },

            viewModel = mainViewModel
        )

        recyclerView.adapter = adapter
        foodsViewModelApi.getCategory(category)
        foodsViewModelApi.responseCategoryMie.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progresbarHome.isVisible = false
                    binding.recycleviewFood.isVisible = true
                    val listFood = it.payload ?: emptyList()
                    adapter.setData(listFood)
                    binding.recycleviewFood.itemAnimator = null
                },
                doOnLoading = {
                    binding.recycleviewFood.isVisible = false
                    binding.progresbarHome.isVisible = true
                },
                doOnError = {
                    Toast.makeText(requireContext(), "Data Food Empty", Toast.LENGTH_LONG).show()
                    binding.progresbarHome.isVisible = false
                }
            )
        }
    }

    private fun setUpRecycleview() {
        val recyclerView = binding.recycleviewFood
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FoodAdapter(
            onItemClick = { food ->
                navigateToDetail(food)
            },

            viewModel = mainViewModel
        )
        recyclerView.adapter = adapter
        foodsViewModelApi.getFoods()
        foodsViewModelApi.responseFood.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progresbarHome.isVisible = false
                    binding.recycleviewFood.isVisible = true
                    val listFood = it.payload ?: emptyList()
                    adapter.setData(listFood)
                },
                doOnLoading = {
                    binding.recycleviewFood.isVisible = false
                    binding.progresbarHome.isVisible = true
                },
                doOnError = {
                    Toast.makeText(requireContext(), "Data Food Empty", Toast.LENGTH_LONG).show()
                    binding.progresbarHome.isVisible = false
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
                when (category.nama) {
                    "Burger" -> viewCategory("burger")
                    "Mie" -> viewCategory("mie")
                    "Snack" -> viewCategory("snack")
                    "Minuman" -> viewCategory("minuman")
                    else -> Toast.makeText(
                        requireContext(),
                        "category ${category.nama}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Toast.makeText(requireContext(), "category ${category.nama}", Toast.LENGTH_SHORT)
                    .show()
            }

        )
        recyclerView.adapter = adapter

        foodsViewModelApi.getCategoryList()
        Log.d("ResponseFoodApiCategory", "setUpRecycleview:${foodsViewModelApi.getCategoryList()} ")
        foodsViewModelApi.responseCategory.observe(viewLifecycleOwner) { category ->
            category.proceedWhen(
                doOnSuccess = {
                    binding.recyclerviewCategory.isVisible = true
                    binding.progresbarHome.isVisible = false
                    val listCategory = it.payload ?: emptyList()
                    adapter.submitListCategory(listCategory)
                    Log.d("ResponseFoodApiCategory", "setUpRecycleview:$listCategory")
                },
                doOnLoading = {
                    binding.recyclerviewCategory.isVisible = false
                    binding.progresbarHome.isVisible = true
                },
                doOnError = {
                    Toast.makeText(requireContext(), "Data Category Empty", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
    }

    // detail to activity
    private fun navigateToDetail(item: Food) {
        DetailFoodActivity.startActivity(requireContext(), item)
    }

    private fun setViewList() {
        mainViewModel.getIsLinearView().observe(viewLifecycleOwner) { isLinearView ->
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
