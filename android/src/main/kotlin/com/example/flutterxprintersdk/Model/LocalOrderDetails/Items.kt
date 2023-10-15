package com.example.flutterxprintersdk

import com.example.flutterxprintersdk.Model.LocalOrderDetails.Components
import com.google.gson.annotations.SerializedName


data class Items (

    @SerializedName("comment"    ) var comment    : String?               = null,
    @SerializedName("components" ) var components : ArrayList<Components> = arrayListOf(),
    @SerializedName("id"         ) var id         : Int?                  = null,
    @SerializedName("price"      ) var price      : Double?               = null,
    @SerializedName("sortname"   ) var sortname   : String?               = null,
    @SerializedName("unit"       ) var unit       : Int?                  = null

)