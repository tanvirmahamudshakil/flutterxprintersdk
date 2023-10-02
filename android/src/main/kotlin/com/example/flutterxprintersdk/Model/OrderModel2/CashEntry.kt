package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class CashEntry(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("comment")
    val comment: Any?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("order_id")
    val orderId: Int?,
    @SerializedName("timestamp")
    val timestamp: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)