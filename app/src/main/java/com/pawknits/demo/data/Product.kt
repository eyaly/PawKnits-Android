package com.pawknits.demo.data

import androidx.annotation.DrawableRes
import com.pawknits.demo.R

data class Product(
    val id: Int,
    val name: String,
    @DrawableRes val image: Int,
    val price: Int,
)

val productList = listOf(
    Product(1, "Orange sweater", R.drawable.dog_sweater1, 1),
    Product(2, "Grey blanket", R.drawable.dog_sweater2, 89),
    Product(3, "US scarf", R.drawable.dog_sweater3, 79),
    Product(4, "Grey sweater", R.drawable.dog_sweater4, 94),
    Product(5, "Yellow sweater", R.drawable.dog_sweater5, 99),
    Product(6, "Green sweater", R.drawable.dog_sweater6, 65),
    Product(7, "Xmas sweater", R.drawable.dog_sweater7, 54),
    Product(8, "Grey-Red sweater", R.drawable.dog_sweater8, 83),
)
