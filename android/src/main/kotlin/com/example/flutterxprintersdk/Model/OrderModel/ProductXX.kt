package com.example.flutterxprintersdk.Model.OrderModel

data class ProductXX(
    val barcode: Any,
    val creator_id: Int,
    val creator_uuid: String,
    val description: String,
    val discountable: Int,
    val files: List<File>,
    val id: Int,
    val `property`: PropertyXXX,
    val short_name: String,
    val sort_order: Int,
    val status: Int,
    val tags: String,
    val type: String,
    val uuid: String
)