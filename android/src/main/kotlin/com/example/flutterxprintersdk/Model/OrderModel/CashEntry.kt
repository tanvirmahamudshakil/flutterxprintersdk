package com.example.flutterxprintersdk.Model.OrderModel

data class CashEntry(
    val amount: Double,
    val comment: Any,
    val created_at: String,
    val id: Int,
    val order_id: Int,
    val timestamp: String,
    val type: String,
    val updated_at: String
)