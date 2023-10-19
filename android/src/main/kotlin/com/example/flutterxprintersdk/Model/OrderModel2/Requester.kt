package com.example.example

import com.example.flutterxprintersdk.Model.OrderModel2.RequesterProperty
import com.google.gson.annotations.SerializedName


data class Requester (

  @SerializedName("id"                ) var id              : Int?      = null,
  @SerializedName("uuid"              ) var uuid            : String?   = null,
  @SerializedName("role"              ) var role            : String?   = null,
  @SerializedName("name"              ) var name            : String?   = null,
  @SerializedName("email"             ) var email           : String?   = null,
  @SerializedName("username"          ) var username        : String?   = null,
  @SerializedName("phone"             ) var phone           : String?   = null,
  @SerializedName("email_verified_at" ) var emailVerifiedAt : String?   = null,
  @SerializedName("provider"          ) var provider        : String?   = null,
  @SerializedName("provider_id"       ) var providerId      : String?   = null,
  @SerializedName("created_at"        ) var createdAt       : String?   = null,
  @SerializedName("updated_at"        ) var updatedAt       : String?   = null,
  @SerializedName("property"          ) var property        : RequesterProperty? = null

)