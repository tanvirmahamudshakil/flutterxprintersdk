package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class RequesterGuest(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("uuid")
    val uuid: String?
)