package com.example.flutterxprintersdk.Model.LocalOrderDetails

import com.google.gson.annotations.SerializedName


data class Address (

  @SerializedName("type"     ) var type     : String? = null,
  @SerializedName("building" ) var building : String? = null,
  @SerializedName("street"   ) var street   : String? = null,
  @SerializedName("city"     ) var city     : String? = null,
  @SerializedName("postcode" ) var postcode : String? = null

)