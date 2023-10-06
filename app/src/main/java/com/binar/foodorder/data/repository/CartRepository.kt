package com.binar.foodorder.data.repository

import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.data.local.database.mapper.toCartEntity
import com.binar.foodorder.data.local.database.mapper.toCartProductList
import com.binar.foodorder.model.Cart
import com.binar.foodorder.model.CartFood
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.ResultWrapper
import com.binar.foodorder.util.proceed
import com.binar.foodorder.util.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {
    fun getUserCartData(): Flow<ResultWrapper<Pair<List<CartFood>, Double>>>
    suspend fun createCart(food: Food, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource
) : CartRepository {

    override fun getUserCartData(): Flow<ResultWrapper<Pair<List<CartFood>, Double>>> {
        return dataSource.getAllCarts().map {
            proceed {
                val cartList = it.toCartProductList()
                val totalPrice = cartList.sumOf {
                    val quantity = it.cart.itemQuantity
                    val pricePerItem = it.food.Price
                    quantity * pricePerItem
                }
                Pair(cartList, totalPrice)
            }
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

    //gunakan di detail
    override suspend fun createCart(
        food: Food,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return food.id?.let { foodId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(foodId = foodId, itemQuantity = totalQuantity)
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

}