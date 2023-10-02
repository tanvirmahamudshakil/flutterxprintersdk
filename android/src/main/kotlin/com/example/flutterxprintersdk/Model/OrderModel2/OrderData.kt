package com.example.flutterxprintersdk.Model.OrderModel2


import com.google.gson.annotations.SerializedName

data class OrderData(
    @SerializedName("branch")
    val branch: Branch?,
    @SerializedName("branch_id")
    val branchId: Int?,
    @SerializedName("cash_entry")
    val cashEntry: List<CashEntry>?,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("delivery_charge")
    val deliveryCharge: Int?,
    @SerializedName("discounted_amount")
    val discountedAmount: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("local_id")
    val localId: Any?,
    @SerializedName("net_amount")
    val netAmount: Double?,
    @SerializedName("order_channel")
    val orderChannel: String?,
    @SerializedName("order_date")
    val orderDate: String?,
    @SerializedName("order_files")
    val orderFiles: List<Any>?,
    @SerializedName("order_products")
    val orderProducts: List<OrderProduct>?,
    @SerializedName("order_type")
    val orderType: String?,
    @SerializedName("payable_amount")
    val payableAmount: Double?,
    @SerializedName("payment")
    val payment: Any?,
    @SerializedName("payment_id")
    val paymentId: Any?,
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("prescriber")
    val prescriber: Any?,
    @SerializedName("prescriber_id")
    val prescriberId: Any?,
    @SerializedName("requested_delivery_timestamp")
    val requestedDeliveryTimestamp: String?,
    @SerializedName("requester")
    val requester: Any?,
    @SerializedName("requester_guest")
    val requesterGuest: RequesterGuest?,
    @SerializedName("requester_id")
    val requesterId: Int?,
    @SerializedName("requester_type")
    val requesterType: String?,
    @SerializedName("requester_uuid")
    val requesterUuid: String?,
    @SerializedName("shipping_address")
    val shippingAddress: Any?,
    @SerializedName("shipping_address_id")
    val shippingAddressId: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)