package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Product (

  @SerializedName("id"           ) var id           : Int?      = null,
  @SerializedName("sort_order"   ) var sortOrder    : Int?      = null,
  @SerializedName("uuid"         ) var uuid         : String?   = null,
  @SerializedName("barcode"      ) var barcode      : String?   = null,
  @SerializedName("type"         ) var type         : String?   = null,
  @SerializedName("short_name"   ) var shortName    : String?   = null,
  @SerializedName("description"  ) var description  : String?   = null,
  @SerializedName("status"       ) var status       : Int?      = null,
  @SerializedName("discountable" ) var discountable : Int?      = null,
  @SerializedName("creator_id"   ) var creatorId    : Int?      = null,
  @SerializedName("creator_uuid" ) var creatorUuid  : String?   = null,
  @SerializedName("tags"         ) var tags         : String?   = null,
  @SerializedName("property"     ) var property     : Property? = Property()

)