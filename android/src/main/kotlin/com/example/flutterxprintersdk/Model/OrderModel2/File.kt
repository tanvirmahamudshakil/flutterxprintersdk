package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class File(
    @SerializedName("file_name")
    val fileName: String?,
    @SerializedName("file_path")
    val filePath: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("type")
    val type: String?
)