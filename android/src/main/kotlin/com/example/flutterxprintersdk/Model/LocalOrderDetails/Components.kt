package com.example.flutterxprintersdk.Model.LocalOrderDetails

import com.google.gson.annotations.SerializedName


data class Components (

  @SerializedName("id"         ) var id         : Int?        = null,
  @SerializedName("shortName"  ) var shortName  : String?     = null,
  @SerializedName("type"       ) var type       : String?     = null,
  @SerializedName("groupName"  ) var groupName  : String?     = null,
  @SerializedName("currency"   ) var currency   : String?     = null,
  @SerializedName("price"      ) var price      : Double?        = null,
  @SerializedName("unit"       ) var unit       : Int?        = null,
  @SerializedName("comment"    ) var comment    : String?     = null,
  @SerializedName("components" ) var components : Components? = null,

)