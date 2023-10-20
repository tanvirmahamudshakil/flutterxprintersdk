package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Branch (

  @SerializedName("id"         ) var id        : Int?      = null,
  @SerializedName("name"       ) var name      : String?   = null,
  @SerializedName("value"      ) var value     : String?   = null,
  @SerializedName("created_at" ) var createdAt : String?   = null,
  @SerializedName("updated_at" ) var updatedAt : String?   = null,
  @SerializedName("property"   ) var property  : Property? = null

)