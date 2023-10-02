package com.example.flutterxprintersdk.Model.OrderModel

data class OrderModel(
    val branch: Branch,
    val branch_id: Int,
    val cash_entry: List<Any>,
    val comment: String,
    val created_at: String,
    val delivery_charge: Int,
    val discounted_amount: Double,
    val id: Int,
    val local_id: Any,
    val net_amount: Double,
    val order_channel: String,
    val order_date: String,
    val order_files: List<Any>,
    val order_products: List<OrderProduct>,
    val order_type: String,
    val payable_amount: Double,
    val payment: Any,
    val payment_id: Any,
    val payment_type: String,
    val prescriber: Any,
    val prescriber_id: Any,
    val requested_delivery_timestamp: String,
    val requester: Any,
    val requester_guest: RequesterGuest,
    val requester_id: Int,
    val requester_type: String,
    val requester_uuid: String,
    val shipping_address: Any,
    val shipping_address_id: Any,
    val status: String,
    val updated_at: String
)