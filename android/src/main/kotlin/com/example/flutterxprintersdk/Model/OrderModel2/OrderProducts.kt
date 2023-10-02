package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class OrderProducts (

  @SerializedName("id"                  ) var id                 : Int?                  = null,
  @SerializedName("parent_id"           ) var parentId           : String?               = null,
  @SerializedName("order_id"            ) var orderId            : Int?                  = null,
  @SerializedName("product_id"          ) var productId          : Int?                  = null,
  @SerializedName("parent_product_id"   ) var parentProductId    : String?               = null,
  @SerializedName("unit"                ) var unit               : Int?                  = null,
  @SerializedName("net_amount"          ) var netAmount          : Double?               = null,
  @SerializedName("discountable_amount" ) var discountableAmount : Int?                  = null,
  @SerializedName("comment"             ) var comment            : String?               = null,
  @SerializedName("product"             ) var product            : Product?              = Product(),
  @SerializedName("components"          ) var components         : ArrayList<Components> = arrayListOf()

)