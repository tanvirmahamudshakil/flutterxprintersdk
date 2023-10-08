package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Property (

  @SerializedName("phone"    ) var phone    : String? = null,
  @SerializedName("email"    ) var email    : String? = null,
  @SerializedName("address"  ) var address  : String? = null,
  @SerializedName("postcode" ) var postcode : String? = null

)