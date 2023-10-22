package com.binar.foodorder.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.binar.foodorder.data.network.api.CategoryNetworkDataSource
import com.binar.foodorder.data.network.api.CategoryNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSourceImpl
import com.binar.foodorder.data.network.api.FoodService
import com.binar.foodorder.data.repository.FoodRepository
import com.binar.foodorder.data.repository.FoodRepositoryImpl
import com.binar.foodorder.data.repository.FoodsRepository
import com.binar.foodorder.data.repository.FoodsRepositoryImpl
import com.binar.foodorder.databinding.FragmentHomeFoodBinding
import com.binar.foodorder.model.Category
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.viewmodel.DatastoreViewModel
import com.binar.foodorder.viewmodel.FoodViewModel
import com.binar.foodorder.viewmodel.FoodsViewModel
import com.binar.foodorder.viewmodel.MainViewModel

class HomeFood : Fragment() {

    private lateinit var binding: FragmentHomeFoodBinding
//    private val foodsViewModel: FoodViewModel by viewModels {
////        val databaseDataSource = AppDatabase.getInstance(requireContext())
////        val categoryDataSource: DummyCategoryDataSource = DummyCategoryDataSourceImpl()
//////        val foodDao = databaseDataSource.foodDao()
//////        val foodDataSource = FoodDatabaseDataSource(foodDao)
//////        val foodRepository: FoodRepository = FoodRepositoryImpl( categoryDataSource)
////        GenericViewModelFactory.create(FoodViewModel(foodRepository))
//    }

    private val foodsViewModelApi: FoodsViewModel by viewModels {
        val service: FoodService by lazy {
            FoodService.invoke()
        }
        val databaseDataSource = AppDatabase.getInstance(requireContext())
        val cdf:CategoryNetworkDataSource = CategoryNetworkDataSourceImpl(service)
        val nds: FoodNetworkDataSource = FoodNetworkDataSourceImpl(service)
        val repo: FoodsRepository = FoodsRepositoryImpl(nds,cdf,databaseDataSource)
        GenericViewModelFactory.create(FoodsViewModel(repo))
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
        setUpRecycleviewCategory()
        setUpRecycleview()

        Log.d(requireContext().toString(), "Category:${setUpRecycleviewCategory()} ")
        setViewList()
//        testCategoryMie("mie")
    }

    private fun viewCategory(category: String) {
        val recyclerView = binding.recycleviewFood
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FoodAdapter(
            onItemClick = { food ->
                navigateToDetail(food)
            },

            viewModel = viewDataStoreViewModel
        )
        recyclerView.adapter = adapter
        foodsViewModelApi.getCategory(category)
        foodsViewModelApi.responseCategoryMie.observe(viewLifecycleOwner){result->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progresbarHome.isVisible = false
                    binding.recycleviewFood.isVisible = true
                    val listFood = it.payload?: emptyList()
                    adapter.setData(listFood)
                    binding.recycleviewFood.itemAnimator = null
//                    Log.d("ResponseFoodApi", "setUpRecycleview:$listFood ")
//                    result.payload?.data.let { foods ->
//                        adapter.setData(foods)
//                    }

                }, doOnLoading = {
                    binding.recycleviewFood.isVisible = false
                    binding.progresbarHome.isVisible = true

                }, doOnError = {
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

            viewModel = viewDataStoreViewModel
        )
        recyclerView.adapter = adapter
        foodsViewModelApi.getFoods()
        foodsViewModelApi.responseFood.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progresbarHome.isVisible = false
                    binding.recycleviewFood.isVisible = true
                   val listFood = it.payload?: emptyList()
                    adapter.setData(listFood)
//                    Log.d("ResponseFoodApi", "setUpRecycleview:$listFood ")
//                    result.payload?.data.let { foods ->
//                        adapter.setData(foods)
//                    }

                }, doOnLoading = {
                    binding.recycleviewFood.isVisible = false
                    binding.progresbarHome.isVisible = true

                }, doOnError = {
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
                when(category.nama){
                    "Burger" -> viewCategory("burger")
                    "Mie" -> viewCategory("mie")
                    "Snack" -> viewCategory("snack")
                    "Minuman" -> viewCategory("minuman")
                    else->  Toast.makeText(requireContext(), "category ${category.nama}", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(requireContext(), "category ${category.nama}", Toast.LENGTH_SHORT).show()
            },

            )
        recyclerView.adapter = adapter

        foodsViewModelApi.getCategoryList()
        Log.d("ResponseFoodApiCategory", "setUpRecycleview:${foodsViewModelApi.getCategoryList()} ")
        foodsViewModelApi.responseCategory.observe(viewLifecycleOwner) { category ->
            category.proceedWhen(
                doOnSuccess = {
                    binding.recyclerviewCategory.isVisible = true
                    binding.progresbarHome.isVisible = false
                    val listCategory = it.payload?: emptyList()
                    adapter.submitListCategory(listCategory)
                    Log.d("ResponseFoodApiCategory", "setUpRecycleview:$listCategory")
                }, doOnLoading = {
                    binding.recyclerviewCategory.isVisible = false
                    binding.progresbarHome.isVisible = true

                }, doOnError = {
                    Toast.makeText(requireContext(), "Data Category Empty", Toast.LENGTH_SHORT).show()
                }
            )
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
