package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Componentss (

  @SerializedName("id"                  ) var id                 : Int?                  = null,
  @SerializedName("parent_id"           ) var parentId           : Int?                  = null,
  @SerializedName("order_id"            ) var orderId            : Int?                  = null,
  @SerializedName("product_id"          ) var productId          : Int?                  = null,
  @SerializedName("parent_product_id"   ) var parentProductId    : Int?                  = null,
  @SerializedName("unit"                ) var unit               : Int?                  = null,
  @SerializedName("net_amount"          ) var netAmount          : Double?               = null,
  @SerializedName("discountable_amount" ) var discountableAmount : Int?                  = null,
  @SerializedName("comment"             ) var comment            : String?               = null,
  @SerializedName("components"          ) var components         : ArrayList<Componentss> = arrayListOf(),
  @SerializedName("product"             ) var product            : Product?              = Product()

)