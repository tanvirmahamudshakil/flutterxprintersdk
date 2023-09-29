package com.example.flutterxprintersdk.Model.OrderModel

data class Component(
    val comment: Any,
    val components: List<ComponentX>,
    val discountable_amount: Int,
    val id: Int,
    val net_amount: Double,
    val order_id: Int,
    val parent_id: Int,
    val parent_product_id: Int,
    val product: ProductX,
    val product_id: Int,
    val unit: Int
)