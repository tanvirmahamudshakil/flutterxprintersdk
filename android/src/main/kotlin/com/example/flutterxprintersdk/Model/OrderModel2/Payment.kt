package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Payment (

  @SerializedName("id"             ) var id            : Int?    = null,
  @SerializedName("requester_type" ) var requesterType : String? = null,
  @SerializedName("requester_id"   ) var requesterId   : Int?    = null,
  @SerializedName("reference"      ) var reference     : String? = null,
  @SerializedName("payment_date"   ) var paymentDate   : String? = null,
  @SerializedName("amount"         ) var amount        : Int?    = null,
  @SerializedName("source"         ) var source        : String? = null,
  @SerializedName("card_type"      ) var cardType      : String? = null,
  @SerializedName("status"         ) var status        : String? = null,
  @SerializedName("comment"        ) var comment       : String? = null,
  @SerializedName("created_at"     ) var createdAt     : String? = null,
  @SerializedName("updated_at"     ) var updatedAt     : String? = null

)