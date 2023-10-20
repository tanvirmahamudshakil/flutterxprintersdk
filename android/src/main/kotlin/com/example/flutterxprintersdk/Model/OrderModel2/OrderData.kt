package com.example.flutterxprintersdk

import com.example.example.Requester
import com.example.example.ShippingAddress
import com.google.gson.annotations.SerializedName


data class OrderData (

  @SerializedName("id"                           ) var id                         : Int?                     = null,
  @SerializedName("local_id"                     ) var localId                    : String?                  = null,
  @SerializedName("order_type"                   ) var orderType                  : String?                  = null,
  @SerializedName("order_channel"                ) var orderChannel               : String?                  = null,
  @SerializedName("order_date"                   ) var orderDate                  : String?                  = null,
  @SerializedName("requester_type"               ) var requesterType              : String?                  = null,
  @SerializedName("requester_id"                 ) var requesterId                : Int?                     = null,
  @SerializedName("requester_uuid"               ) var requesterUuid              : String?                  = null,
  @SerializedName("shipping_address_id"          ) var shippingAddressId          : String?                  = null,
  @SerializedName("requested_delivery_timestamp" ) var requestedDeliveryTimestamp : String?                  = null,
  @SerializedName("status"                       ) var status                     : String?                  = null,
  @SerializedName("net_amount"                   ) var netAmount                  : Double?                  = null,
  @SerializedName("discounted_amount"            ) var discountedAmount           : Double?                  = null,
  @SerializedName("delivery_charge"              ) var deliveryCharge             : Double?                  = null,
  @SerializedName("payable_amount"               ) var payableAmount              : Double?                  = null,
  @SerializedName("payment_type"                 ) var paymentType                : String?                  = null,
  @SerializedName("payment_id"                   ) var paymentId                  : String?                  = null,
  @SerializedName("prescriber_id"                ) var prescriberId               : String?                  = null,
  @SerializedName("branch_id"                    ) var branchId                   : Int?                     = null,
  @SerializedName("comment"                      ) var comment                    : String?                  = null,
  @SerializedName("created_at"                   ) var createdAt                  : String?                  = null,
  @SerializedName("updated_at"                   ) var updatedAt                  : String?                  = null,
  @SerializedName("order_products"               ) var orderProducts              : ArrayList<OrderProducts> = arrayListOf(),
  @SerializedName("requester"                    ) var requester                  : Requester?               = null,
  @SerializedName("requester_guest"              ) var requesterGuest             : RequesterGuest?          = null,
  @SerializedName("shipping_address"             ) var shippingAddress            : ShippingAddress?          = null,
  @SerializedName("order_files"                  ) var orderFiles                 : ArrayList<String>        = arrayListOf(),
  @SerializedName("prescriber"                   ) var prescriber                 : String?                  = null,
  @SerializedName("payment"                      ) var payment                    : String?                  = null,
  @SerializedName("cash_entry"                   ) var cashEntry                  : ArrayList<CashEntry>     = arrayListOf(),
  @SerializedName("branch"                       ) var branch                     : Branch?                  = Branch()

)