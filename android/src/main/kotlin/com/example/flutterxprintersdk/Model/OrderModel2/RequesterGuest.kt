package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class RequesterGuest (

  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("uuid"       ) var uuid      : String? = null,
  @SerializedName("first_name" ) var firstName : String? = null,
  @SerializedName("last_name"  ) var lastName  : String? = null,
  @SerializedName("email"      ) var email     : String? = null,
  @SerializedName("phone"      ) var phone     : String? = null,
  @SerializedName("created_at" ) var createdAt : String? = null,
  @SerializedName("updated_at" ) var updatedAt : String? = null

)