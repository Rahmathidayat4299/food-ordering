package com.binar.foodorder.data.dummy

import com.binar.foodorder.model.Category

interface DummyCategoryDataSource {
    fun getFoodCategory(): List<Category>
}


class DummyCategoryDataSourceImpl : DummyCategoryDataSource {
    override fun getFoodCategory(): List<Category> {
        return listOf(
//            Category(
//                name = "Nasi",
//                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/nasi.png"
//            ),
//            Category(
//                name = "Snack",
//                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/snack.png"
//            ),
//            Category(
//                name = "Minuman",
//                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/minuman.png"
//            ),
//            Category(
//                name = "Mie",
//                categoryImgUrl = "https://raw.githubusercontent.com/Rahmathidayat4299/assetfood/master/mie.png"
//            )
        )
    }
}