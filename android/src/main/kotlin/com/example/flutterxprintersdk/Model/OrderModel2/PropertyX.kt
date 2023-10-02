package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class PropertyX(
    @SerializedName("is_coupon")
    val isCoupon: String?,
    @SerializedName("platform")
    val platform: String?
)