package com.example.flutterxprintersdk.Model.LocalOrderDetails

import com.google.gson.annotations.SerializedName


data class Components (

  @SerializedName("comment"    ) var comment    : String?               = null,
  @SerializedName("components" ) var components : ArrayList<Components> = arrayListOf(),
  @SerializedName("id"         ) var id         : Int?                  = null,
  @SerializedName("price"      ) var price      : Int?                  = null,
  @SerializedName("sortname"   ) var sortname   : String?               = null,
  @SerializedName("unit"       ) var unit       : Int?                  = null

)