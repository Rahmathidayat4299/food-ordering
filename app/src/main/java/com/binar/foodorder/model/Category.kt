package com.binar.foodorder.model

import java.util.UUID

/**
 * Created by Rahmat Hidayat on 02/10/2023.
 */
data class Category(
    val id: String = UUID.randomUUID().toString(),
    val categoryImgUrl: String,
    val name: String
)