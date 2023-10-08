package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class CashEntry (

  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("timestamp"  ) var timestamp : String? = null,
  @SerializedName("type"       ) var type      : String? = null,
  @SerializedName("order_id"   ) var orderId   : Int?    = null,
  @SerializedName("amount"     ) var amount    : Double? = null,
  @SerializedName("comment"    ) var comment   : String? = null,
  @SerializedName("created_at" ) var createdAt : String? = null,
  @SerializedName("updated_at" ) var updatedAt : String? = null

)