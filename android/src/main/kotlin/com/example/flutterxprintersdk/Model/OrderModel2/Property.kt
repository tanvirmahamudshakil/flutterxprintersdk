package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("address")
    val address: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("postcode")
    val postcode: String?
)