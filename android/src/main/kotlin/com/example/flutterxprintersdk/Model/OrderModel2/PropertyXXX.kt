package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class PropertyXXX(
    @SerializedName("category")
    val category: String?,
    @SerializedName("discount_type")
    val discountType: String?,
    @SerializedName("discount_value")
    val discountValue: String?,
    @SerializedName("epos_category")
    val eposCategory: String?,
    @SerializedName("featured")
    val featured: String?,
    @SerializedName("is_coupon")
    val isCoupon: String?,
    @SerializedName("platform")
    val platform: String?
)