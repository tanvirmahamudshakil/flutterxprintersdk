package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class Branch(
    @SerializedName("created_at")
    val createdAt: Any?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("property")
    val `property`: Property?,
    @SerializedName("updated_at")
    val updatedAt: Any?,
    @SerializedName("value")
    val value: String?
)