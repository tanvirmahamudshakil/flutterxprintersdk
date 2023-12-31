package com.example.flutterxprintersdk.Model.LocalOrderDetails

import com.google.gson.annotations.SerializedName


data class LocalOrderDetails (

  @SerializedName("comment"                    ) var comment                    : String?          = null,
  @SerializedName("createdAt"                  ) var createdAt                  : String?          = null,
  @SerializedName("deliveryCharge"             ) var deliveryCharge             : Double?             = null,
  @SerializedName("discountedAmount"           ) var discountedAmount           : Double?             = null,
  @SerializedName("id"                         ) var id                         : Int?             = null,
  @SerializedName("localId"                    ) var localId                    : Int?             = null,
  @SerializedName("netAmount"                  ) var netAmount                  : Double?          = null,
  @SerializedName("orderChannel"               ) var orderChannel               : String?          = null,
  @SerializedName("orderDate"                  ) var orderDate                  : String?          = null,
  @SerializedName("orderType"                  ) var orderType                  : String?          = null,
  @SerializedName("payableAmount"              ) var payableAmount              : Double?          = null,
  @SerializedName("paymentId"                  ) var paymentId                  : Int?             = null,
  @SerializedName("paymentType"                ) var paymentType                : String?          = null,
  @SerializedName("prescriberId"               ) var prescriberId               : String?          = null,
  @SerializedName("requestedDeliveryTimestamp" ) var requestedDeliveryTimestamp : String?          = null,
  @SerializedName("requesterId"                ) var requesterId                : Int?             = null,
  @SerializedName("requesterType"              ) var requesterType              : String?          = null,
  @SerializedName("requesterUuid"              ) var requesterUuid              : String?          = null,
  @SerializedName("shippingAddressId"          ) var shippingAddressId          : Int?             = null,
  @SerializedName("status"                     ) var status                     : String?          = null,
  @SerializedName("updatedAt"                  ) var updatedAt                  : String?          = null,
  @SerializedName("items"                      ) var items                      : ArrayList<Items> = arrayListOf(),
  @SerializedName("customer"                   ) var customer                   : Customer?        = null

)