package com.kharedji.onlineshopping.domain.models

class OrderModel(
    var orderId:String="",
    var orderDate:String="",
    var cartsModel:HashMap<String,CartModel> = hashMapOf(),
    var total_products:Int=0,
    var total_price:Int=0,
    var userId:String=""
){
}