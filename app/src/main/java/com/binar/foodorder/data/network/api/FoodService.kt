package com.binar.foodorder.data.network.api

import com.binar.foodorder.BuildConfig
import com.binar.foodorder.data.network.model.CategoryViewParamResponse
import com.binar.foodorder.data.network.model.FoodResponse
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.network.model.OrderResponse
import com.binar.foodorder.model.Category
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FoodService {
    @GET("listmenu")
    suspend fun getFoods(): FoodResponse
    @GET("category")
    suspend fun getListCategories():CategoryViewParamResponse

    @GET("listmenu?")
    suspend fun getCategories(@Query("c") category:String):FoodResponse

    @POST("order")
    suspend fun createOrder(@Body orderRequest: OrderRequest):OrderResponse
    companion object {
        @JvmStatic
        operator fun invoke(): FoodService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(FoodService::class.java)
        }
    }
}