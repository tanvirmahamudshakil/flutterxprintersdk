package com.example.flutterxprintersdk.Model.OrderModel

data class OrderProduct(
    val comment: String,
    val components: List<Component>,
    val discountable_amount: Int,
    val id: Int,
    val net_amount: Double,
    val order_id: Int,
    val parent_id: Any,
    val parent_product_id: Any,
    val product: ProductXX,
    val product_id: Int,
    val unit: Int
)