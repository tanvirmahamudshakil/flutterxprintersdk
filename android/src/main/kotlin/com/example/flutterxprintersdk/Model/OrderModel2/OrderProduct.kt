package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class OrderProduct(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("components")
    val components: List<Component>?,
    @SerializedName("discountable_amount")
    val discountableAmount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("net_amount")
    val netAmount: Double?,
    @SerializedName("order_id")
    val orderId: Int?,
    @SerializedName("parent_id")
    val parentId: Any?,
    @SerializedName("parent_product_id")
    val parentProductId: Any?,
    @SerializedName("product")
    val product: ProductXX?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("unit")
    val unit: Int?
)