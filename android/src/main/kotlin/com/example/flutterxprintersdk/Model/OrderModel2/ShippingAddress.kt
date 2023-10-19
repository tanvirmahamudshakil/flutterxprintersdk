package com.example.example

import com.example.flutterxprintersdk.Model.OrderModel2.AddressProperty
import com.example.flutterxprintersdk.Property
import com.google.gson.annotations.SerializedName


data class ShippingAddress (

  @SerializedName("id"           ) var id          : Int?      = null,
  @SerializedName("name"         ) var name        : String?   = null,
  @SerializedName("type"         ) var type        : String?   = null,
  @SerializedName("creator_type" ) var creatorType : String?   = null,
  @SerializedName("creator_id"   ) var creatorId   : Int?      = null,
  @SerializedName("status"       ) var status      : Int?      = null,
  @SerializedName("created_at"   ) var createdAt   : String?   = null,
  @SerializedName("updated_at"   ) var updatedAt   : String?   = null,
  @SerializedName("property"     ) var addressProperty : AddressProperty? = null

)