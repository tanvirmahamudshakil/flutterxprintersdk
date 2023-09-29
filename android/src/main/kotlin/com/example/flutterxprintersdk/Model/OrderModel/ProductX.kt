package com.example.flutterxprintersdk.Model.OrderModel

data class ProductX(
    val barcode: Any,
    val creator_id: Int,
    val creator_uuid: String,
    val description: Any,
    val discountable: Int,
    val id: Int,
    val `property`: PropertyX,
    val short_name: String,
    val sort_order: Int,
    val status: Int,
    val tags: Any,
    val type: String,
    val uuid: String
)