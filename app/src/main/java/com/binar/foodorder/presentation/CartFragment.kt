package com.binar.foodorder.presentation


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.adapter.CartListAdapter
import com.binar.foodorder.adapter.CartListener
import com.binar.foodorder.adapter.FoodAdapter
import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.datasource.CartDatabaseDataSource
import com.binar.foodorder.data.local.datastore.ViewDataStoreManager
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.data.repository.CartRepositoryImpl
import com.binar.foodorder.databinding.FragmentCartBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import com.binar.foodorder.viewmodel.CartViewModel
import com.binar.foodorder.viewmodel.DatastoreViewModel


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    //    private val foodsViewModel: FoodViewModel by viewModels {
////        val foodDataSource: FoodDataSource = FoodDataSourceImpl()
////        val categoryDataSource: CategoryDataSource = CategoryDataSourceImpl()
////        val foodRepository: FoodRepository = FoodRepositoryImpl(foodDataSource,categoryDataSource)
////        GenericViewModelFactory.create(FoodViewModel(foodRepository))
//    }
    private val viewDataStoreViewModel: DatastoreViewModel by viewModels {
        val vds: ViewDataStoreManager = ViewDataStoreManager(requireContext())
        GenericViewModelFactory.create(DatastoreViewModel(vds))
    }
    private val viewModel: CartViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource)
        GenericViewModelFactory.create(CartViewModel(repo))
    }
    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }

            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }

            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.removeCart(cart)
            }

            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.setCartNotes(cart)
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleview()
        navigateToOrdeConfirmation()
    }

    private fun setUpRecycleview() {
        val recyclerView = binding.recyclerviewCart
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        val adapter = FoodAdapter(
//            onItemClick = { food ->
//                navigateToDetail(food)
//            },
//
//            viewModel = viewDataStoreViewModel
//        )
        recyclerView.adapter = adapter
        viewModel.cartList.observe(viewLifecycleOwner) { result ->
            result.payload?.let { (carts, totalPrice) ->
                adapter.submitData(carts)
                binding.tvTotalValue.text = totalPrice.toCurrencyFormat()
            }
        }
//        foodsViewModel.foods.observe(viewLifecycleOwner) { foods ->
//            adapter.setData(foods)
//        }
    }

    //detail to activity
    private fun navigateToDetail(item: Food) {
        DetailFoodActivity.startActivity(requireContext(), item)
    }

    private fun navigateToOrdeConfirmation() {
        binding.btnOrder.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmationOrderActivity::class.java)
            startActivity(intent)
        }
    }


}