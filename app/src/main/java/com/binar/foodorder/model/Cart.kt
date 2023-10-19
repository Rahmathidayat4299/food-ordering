package com.binar.foodorder.model

import com.binar.foodorder.data.local.database.mapper.toCart

data class Cart(
    var id: Int = 0,
    var foodId : Int = 0,
    var itemQuantity: Int = 0,
    var itemNotes: String? = null,
    val foodName:String?= null,
    val foodPrice:Double,
    val  foodImgUrl :String? = null,

)