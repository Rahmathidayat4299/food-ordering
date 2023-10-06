package com.binar.foodorder.data.local.database.datasource

import com.binar.foodorder.data.local.database.dao.CartDao
import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.data.local.database.relation.CartFoodRelation
import kotlinx.coroutines.flow.Flow

interface CartDataSource {
    fun getAllCarts(): Flow<List<CartFoodRelation>>
    fun getCartById(cartId: Int): Flow<CartFoodRelation>
    suspend fun insertCart(cart: CartEntity): Long
    suspend fun deleteCart(cart: CartEntity): Int
    suspend fun updateCart(cart: CartEntity): Int
}

class CartDatabaseDataSource(private val cartDao: CartDao) : CartDataSource {
    override fun getAllCarts(): Flow<List<CartFoodRelation>> {
        return cartDao.getAllCarts()
    }

    override fun getCartById(cartId: Int): Flow<CartFoodRelation> {
        return cartDao.getCartById(cartId)
    }

    override suspend fun insertCart(cart: CartEntity): Long {
        return cartDao.insertCart(cart)
    }

    override suspend fun deleteCart(cart: CartEntity): Int {
        return cartDao.deleteCart(cart)
    }

    override suspend fun updateCart(cart: CartEntity): Int {
        return cartDao.updateCart(cart)
    }

}