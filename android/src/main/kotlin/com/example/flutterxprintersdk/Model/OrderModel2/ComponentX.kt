package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class ComponentX(
    @SerializedName("comment")
    val comment: Any?,
    @SerializedName("components")
    val components: List<Any>?,
    @SerializedName("discountable_amount")
    val discountableAmount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("net_amount")
    val netAmount: Int?,
    @SerializedName("order_id")
    val orderId: Int?,
    @SerializedName("parent_id")
    val parentId: Int?,
    @SerializedName("parent_product_id")
    val parentProductId: Int?,
    @SerializedName("product")
    val product: ProductX?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("unit")
    val unit: Int?
)