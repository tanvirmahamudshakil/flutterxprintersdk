package com.example.flutterxprintersdk.Model.LocalOrderDetails

import com.google.gson.annotations.SerializedName


data class Items (

  @SerializedName("id"                ) var id                : Int?                  = null,
  @SerializedName("unit"              ) var unit              : Int?                  = null,
  @SerializedName("comment"           ) var comment           : String?               = null,
  @SerializedName("shortName"         ) var shortName         : String?               = null,
  @SerializedName("type"              ) var type              : String?               = null,
  @SerializedName("currency"          ) var currency          : String?               = null,
  @SerializedName("price"             ) var price             : Double?               = null,
  @SerializedName("isDiscountApplied" ) var isDiscountApplied : Boolean?               = null,
  @SerializedName("discountPrice"     ) var discountPrice     : String?               = null,
  @SerializedName("components"        ) var components        : ArrayList<Components> = arrayListOf(),
  @SerializedName("extra"             ) var extra             : ArrayList<Components>     = arrayListOf()

)