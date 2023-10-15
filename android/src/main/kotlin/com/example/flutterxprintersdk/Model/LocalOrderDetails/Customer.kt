package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Customer (

  @SerializedName("address"   ) var address   : Address? = Address(),
  @SerializedName("email"     ) var email     : String?  = null,
  @SerializedName("firstName" ) var firstName : String?  = null,
  @SerializedName("lastName"  ) var lastName  : String?  = null,
  @SerializedName("phone"     ) var phone     : String?  = null

)