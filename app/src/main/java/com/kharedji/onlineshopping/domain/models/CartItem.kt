package com.kharedji.onlineshopping.domain.models

data class CartModel(
    val cartId:String="",
    val userId: String = "",
    val product: ProductData = ProductData(),
    val quantity: Int = 0
)
