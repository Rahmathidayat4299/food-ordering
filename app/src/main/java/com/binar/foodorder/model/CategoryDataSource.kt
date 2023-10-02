package com.binar.foodorder.model

/**
 * Created by Rahmat Hidayat on 02/10/2023.
 */

interface CategoryDataSource {
    fun getFoodCategory(): List<Category>
}

class CategoryDataSourceImpl : CategoryDataSource {
    override fun getFoodCategory(): List<Category> {
        return listOf(
            Category(
                name = "Nasi",
                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/nasi.png"
            ),
            Category(
                name = "Snack",
                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/snack.png"
            ),
            Category(
                name = "Minuman",
                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/minuman.png"
            ),
            Category(
                name = "Mie",
                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/mie.png"
            )
        )
    }
}