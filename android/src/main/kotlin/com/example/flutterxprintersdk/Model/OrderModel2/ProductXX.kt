package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class ProductXX(
    @SerializedName("barcode")
    val barcode: Any?,
    @SerializedName("creator_id")
    val creatorId: Int?,
    @SerializedName("creator_uuid")
    val creatorUuid: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("discountable")
    val discountable: Int?,
    @SerializedName("files")
    val files: List<File>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("property")
    val `property`: PropertyXXX?,
    @SerializedName("short_name")
    val shortName: String?,
    @SerializedName("sort_order")
    val sortOrder: Int?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("tags")
    val tags: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uuid")
    val uuid: String?
)