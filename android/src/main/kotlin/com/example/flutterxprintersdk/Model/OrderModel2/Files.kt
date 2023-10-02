package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class Files (

  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("product_id" ) var productId : Int?    = null,
  @SerializedName("file_name"  ) var fileName  : String? = null,
  @SerializedName("type"       ) var type      : String? = null,
  @SerializedName("file_path"  ) var filePath  : String? = null

)