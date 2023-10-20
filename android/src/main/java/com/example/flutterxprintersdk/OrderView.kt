package com.example.flutterxprintersdk

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.example.example.Requester
import com.example.example.ShippingAddress
import com.example.flutterxprintersdk.Model.LocalOrderDetails.Customer
import com.example.flutterxprintersdk.Model.LocalOrderDetails.LocalOrderDetails
import com.example.flutterxprintersdk.Model.OrderModel2.AddressProperty
import com.example.flutterxprintersdk.databinding.ModelPrint2Binding
import com.example.flutterxprintersdk.databinding.ViewPrint2Binding
import com.google.gson.Gson
import java.util.Locale

class OrderView : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var orderjson =  intent.getStringExtra("orderiteam");
        var businessjson = intent.getStringExtra("business");
        var businessdata = Gson().fromJson<PrinterBusinessData>(businessjson, PrinterBusinessData::class.java)
        var orderModel = Gson().fromJson<OrderData>(orderjson, OrderData::class.java)
        val bind: ViewPrint2Binding = ViewPrint2Binding.inflate(LayoutInflater.from(this))
        var allitemsheight = 0
        bind.items.removeAllViews()
//        for (j in orderModel.items!!.indices) {
//            val childView = getView(j, this, 0, 16F, orderModel)
//            bind.items.addView(childView)
//            allitemsheight += childView!!.measuredHeight
//        }
        var paidOrNot = "";

        if (!orderModel.paymentType!!.uppercase().equals("NOTPAY")) {
            paidOrNot ="ORDER IS PAID"
        } else  {
            paidOrNot = "ORDER NOT PAID"
            bind.dueTotalContainer.visibility = View.VISIBLE
            bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
        }
        bind.dueTotalContainer.visibility = View.VISIBLE
        bind.dueTotal.text = "£ " + String.format("%.2f", (orderModel.payableAmount!! - orderModel.discountedAmount!!) + orderModel.deliveryCharge!!)
        var addedDeliveryCharge = 0.0
        bind.orderPaidMessage.text = paidOrNot
        bind.refundContainer.visibility = View.GONE
        val subTotal: Double = orderModel.netAmount!! - addedDeliveryCharge
        bind.subTotal.text = "£ " + String.format( "%.2f", subTotal)
        bind.txtDeliveryCharge.text = "Delivery Charge";
        bind.deliveryCharge.text = "£ " + orderModel.deliveryCharge!!.toFloat().toString()
        bind.cardPayContainer.visibility = View.GONE
        bind.cashPayContainer.visibility = View.GONE
        if (orderModel.orderChannel!!.uppercase() == "ONLINE") {
            if (orderModel.discountedAmount!! > 0) {
                bind.discount.text =
                    "£ " +  String.format( "%.2f",  orderModel.discountedAmount)
            } else bind.discount.text =
                "£ " + String.format("%.2f", 0.0)
        } else {
            var discountStr = "Discount"
            bind.discountTitle.text = discountStr
            var discountAmount = 0.0
            bind.discount.text =
                "£ " + String.format( "%.2f", orderModel.discountedAmount!!.toFloat())
        }
        bind.plasticBagContainer.visibility = View.GONE
        bind.containerBagContainer.visibility = View.GONE
        bind.adjustmentContainer.visibility = View.GONE
        bind.tipsContainer.visibility = View.GONE
        bind.containerOrderNo.visibility = View.VISIBLE
        bind.total.text =
            "£ " +String.format( "%.2f",(orderModel.netAmount!! - orderModel.discountedAmount!!) + orderModel.deliveryCharge!!)
        var dlAddress = "Service charge is not included\n\n"



        if (orderModel.orderType == "DELIVERY" || orderModel.orderType == "COLLECTION" || orderModel.orderType == "TAKEOUT") {
            if (orderModel.requesterGuest != null){
                val customerModel: RequesterGuest? = orderModel.requesterGuest
                dlAddress += "Name : ${customerModel!!.firstName} ${customerModel!!.lastName}\n"
                dlAddress += "Phone : ${customerModel.phone}"
                if (orderModel.orderType != "COLLECTION" && orderModel.orderType != "TAKEOUT"){
                    if (orderModel.shippingAddress != null) {
                        val address: ShippingAddress? = orderModel.shippingAddress
                        if (address!!.addressProperty != null) {
                            val pro: AddressProperty = address.addressProperty!!
                            // CustomerAddressProperties pro = customerModel.addresses.get(0).properties;
                            val building = if (pro.house != null) pro.house else ""
//                    val streetNumber = if (pro.street_number != null) pro.street_number else ""
                            val streetName = if (pro.state != null) pro.state else ""
                            val city = if (pro.town != null) pro.town else ""
                            val state = if (pro.state != null) pro.state else ""
                            val zip = if (pro.postcode != null) pro.postcode else ""
                            dlAddress += "\nAddress : $building $streetName\n$city $state $zip"
                        }
                    }
                }
            }else{
                val customerModel: Requester? = orderModel.requester!!
                dlAddress += "Name : ${customerModel!!.name}\n"
                dlAddress += "Phone : ${customerModel.phone}"
                if (orderModel.orderType != "COLLECTION" && orderModel.orderType != "TAKEOUT"){
                    if (orderModel.shippingAddress != null) {
                        val address: ShippingAddress? = orderModel.shippingAddress
                        if (address!!.addressProperty != null) {
                            val pro: AddressProperty = address.addressProperty!!
                            // CustomerAddressProperties pro = customerModel.addresses.get(0).properties;
                            val building = if (pro.house != null) pro.house else ""
//                    val streetNumber = if (pro.street_number != null) pro.street_number else ""
                            val streetName = if (pro.state != null) pro.state else ""
                            val city = if (pro.town != null) pro.town else ""
                            val state = if (pro.state != null) pro.state else ""
                            val zip = if (pro.postcode != null) pro.postcode else ""
                            dlAddress += "\nAddress : $building $streetName\n$city $state $zip"
                        }
                    }
                }
            }
        }

        var comment = "Comments : ${if(orderModel.comment != null) orderModel.comment else ""}"

        comment += """




        """.trimIndent()

        bind.comments.text = comment
        bind.address.text = dlAddress
        bind.address.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16F)

        setContentView(bind.root)
    }


    fun getView(position: Int, mCtx: Context?, style: Int, fontSize: Float, orderModel: LocalOrderDetails): View? {
        val binding: ModelPrint2Binding = ModelPrint2Binding.inflate(LayoutInflater.from(mCtx))
        val item = orderModel.items!!.get(position)
        val str3 = StringBuilder()
        if (position < orderModel.items!!.size - 1) {
//            if (orderModel.items!![position].components.sortOrder!! < orderModel.orderProducts!![position + 1].product!!.sortOrder!!) {
//                binding.underLine.visibility = View.VISIBLE
//            }
            binding.underLine.visibility = View.VISIBLE
        }
//        if (style == 0) {
//            if (item.components!!.isNotEmpty()) {
//                str3.append(item.unit).append(" x ").append(item.shortName)
//                for (section in item.components) {
//                    var _comName = ""
//                    if (section.shortName!!.uppercase() != "NONE") {
//                        _comName = section.shortName!!
//                    }
//                    if (section.components != null) {
//                        if (section.components!!.shortName!!.uppercase() != "NONE") {
//                            _comName += " -> " + section.components!!.shortName
//                        }
//                    }
//                    if (_comName != "") {
//                        str3.append("\n").append(_comName)
//                    }
//                }
//            } else {
//                str3.append(item.unit).append(" x ").append(item!!.shortName)
//            }
//        } else {
//            if (item.components!!.isNotEmpty()) {
//                for (section in item.components!!) {
//                    var _comName = ""
//                    if (section.shortName != "NONE") {
//                        _comName = section.shortName!!
//                    }
//                    if (section.shortName != "NONE") _comName += " -> " + section.shortName
//                    str3.append(item.unit).append(" x ").append(item.shortName).append(" : ")
//                        .append(_comName)
//                }
//            } else {
//                str3.append(item.unit).append(" x ").append(item.shortName)
//            }
//        }
//        var price = 0.0
//        price = item.price!!
//        if(item.comment != null) str3.append("\nNote : ").append(item.comment)
//        binding.itemText.text = str3.toString()
//        binding.itemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
//        binding.itemPrice.text = "£ ${String.format("%.2f", price.toFloat())}"
//        binding.itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
//        binding.root.buildDrawingCache(true)

        if (style == 0) {
            if (item.components.size > 0) {
                str3.append(item.unit).append(" x ").append(item.shortName)
                for (section in item.components) {
                    var _comName = ""
                    if (!section!!.shortName!!.uppercase().equals("NONE")) {
                        _comName = section!!.shortName.toString();
                    }
                    if (section.components != null) {
                        if (!section.components!!.shortName!!.uppercase().equals("NONE")) {
                            _comName += " -> " + section.components!!.shortName
                        }
                    }
                    if (_comName != "") {
                        str3.append("\n").append(_comName)
                    }
                }
            } else {
                str3.append(item.unit).append(" x ").append(item.shortName)
            }
        } else {
            if (item.components.size > 0) {
                for (section in item.components) {
                    var _comName = ""
                    if (!section.shortName!!.uppercase().equals("NONE")) {
                        _comName = section!!.shortName.toString()
                    }
                    if (section.components != null) {
                        if (!section.components!!.shortName!!.uppercase().equals("NONE")) _comName += " -> " + section.components!!.shortName
                    }
                    str3.append(item.unit).append(" x ").append(item.shortName).append(" : ")
                        .append(_comName)
                }
            } else {
                str3.append(item.unit).append(" x ").append(item.shortName)
            }
        }

        if (item.extra.size > 0) {
            val str = java.lang.StringBuilder("\nExtra :")
            for (extraItem in item.extra) {
                str.append("  *").append(extraItem.shortName)
            }
            str3.append(str.toString())
        }


        var price = 0.0

//        if (item.isDiscountApplied == null || !item.isDiscountApplied!!) {
//            price = item.price!! * item.unit!!
//            for (components in item.components){
//                price += components.price!!
//            }
//            for (extraItem in item.extra) {
//                price += extraItem.price!!
//            }
//        } else price = item.price!!
        if (item.isDiscountApplied != null && item.isDiscountApplied == true){
            price = item.discountPrice!!.toDouble() * item.unit!!
        }else{
            price += item.price!! * item.unit!!;
            if (item.components != null) {
                if (item.components!!.isNotEmpty()) {
                    for ( component in item.components!!) {
                        price += component.price!!;
                    if (component.components != null) {
                        price += component.components!!.price!!;
                    }
                }
                }
            }
        }



        if (item.comment!!.isNotEmpty()) str3.append("\nNote : ").append(item.comment)
        binding.itemText.text = str3.toString()
        binding.itemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        binding.itemPrice.text =
            "£ " + String.format(Locale.getDefault(), "%.2f", price)
        binding.itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
        binding.root.buildDrawingCache(true)

        return binding.root
    }
}