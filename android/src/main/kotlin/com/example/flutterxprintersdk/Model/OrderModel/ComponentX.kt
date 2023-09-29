package com.example.flutterxprintersdk.Model.OrderModel

data class ComponentX(
    val comment: Any,
    val components: List<Any>,
    val discountable_amount: Int,
    val id: Int,
    val net_amount: Int,
    val order_id: Int,
    val parent_id: Int,
    val parent_product_id: Int,
    val product: ProductX,
    val product_id: Int,
    val unit: Int
)