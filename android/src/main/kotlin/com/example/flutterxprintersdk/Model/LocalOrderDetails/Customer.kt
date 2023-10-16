package com.example.flutterxprintersdk.Model.LocalOrderDetails

import com.google.gson.annotations.SerializedName


data class Customer (

  @SerializedName("firstName" ) var firstName : String?  = null,
  @SerializedName("lastName"  ) var lastName  : String?  = null,
  @SerializedName("phone"     ) var phone     : String?  = null,
  @SerializedName("email"     ) var email     : String?  = null,
  @SerializedName("address"   ) var address   : Address? = null,

)