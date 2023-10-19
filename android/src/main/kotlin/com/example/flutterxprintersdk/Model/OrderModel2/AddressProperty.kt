package com.example.flutterxprintersdk.Model.OrderModel2

import com.google.gson.annotations.SerializedName
data class AddressProperty(
    @SerializedName("house"    ) var house    : String? = null,
    @SerializedName("town"     ) var town     : String? = null,
    @SerializedName("state"    ) var state    : String? = null,
    @SerializedName("postcode" ) var postcode : String? = null,
    @SerializedName("address"  ) var address  : String? = null

)
