package com.binar.foodorder.model

data class Cart(
    var id: Int = 0,
    var foodId: Int = 0,
    var itemQuantity: Int = 0,
    var itemNotes: String,
    val foodName: String? = null,
    val foodPrice: Double,
    val foodImgUrl: String? = null

)
