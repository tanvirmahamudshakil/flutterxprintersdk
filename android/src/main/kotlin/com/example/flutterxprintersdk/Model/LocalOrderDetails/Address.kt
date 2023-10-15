package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Address (

  @SerializedName("building" ) var building : String? = null,
  @SerializedName("city"     ) var city     : String? = null,
  @SerializedName("postcode" ) var postcode : String? = null,
  @SerializedName("street"   ) var street   : String? = null,
  @SerializedName("type"     ) var type     : String? = null

)