package com.binar.foodorder.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.foodorder.adapter.CartListAdapter
import com.binar.foodorder.adapter.CartListener
import com.binar.foodorder.databinding.FragmentCartBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.model.Food
import com.binar.foodorder.presentation.confirmationorder.ConfirmationOrderActivity
import com.binar.foodorder.presentation.detailfood.DetailFoodActivity
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.util.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModel()
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        recyclerView.adapter = adapter
        viewModel.cartList.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progresbarCart.isVisible = false
                    binding.tvListCartEmpty.isVisible = false
                    binding.recyclerviewCart.isVisible = true
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvTotalValue.text = totalPrice.toCurrencyFormat()
                    }
                },
                doOnLoading = {
                    binding.tvListCartEmpty.isVisible = false
                    binding.progresbarCart.isVisible = true
                    binding.recyclerviewCart.isVisible = false
                },
                doOnError = {
                },
                doOnEmpty = {
                    binding.progresbarCart.isVisible = false
                    binding.recyclerviewCart.isVisible = false
                    binding.tvListCartEmpty.isVisible = true
                    result.payload?.let { (_, totalPrice) ->
                        binding.tvTotalValue.text = totalPrice.toCurrencyFormat()
                    }
                }
            )
        }
    }
    private fun navigateToOrdeConfirmation() {
        binding.btnOrder.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmationOrderActivity::class.java)
            startActivity(intent)
        }
    }
}
