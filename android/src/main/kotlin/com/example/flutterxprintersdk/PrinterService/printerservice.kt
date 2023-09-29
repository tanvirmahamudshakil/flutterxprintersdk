package com.example.flutterxprintersdk.PrinterService

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.flutterxprintersdk.Model.OrderModel.OrderModel
import com.example.flutterxprintersdk.Model.OrderModel.OrderProduct
import com.example.flutterxprintersdk.Model.OrderModel.RequesterGuest
import com.example.flutterxprintersdk.databinding.ModelPrint2Binding
import com.example.flutterxprintersdk.databinding.ViewPrint2Binding
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.zxy.tiny.Tiny
import com.zxy.tiny.Tiny.BitmapCompressOptions
import java.text.SimpleDateFormat
import java.util.Locale

class printerservice(mcontext: Context, morderModel: OrderModel, businessname : String, businessaddress: String, fontsize : Int,businessphone : String) {

    lateinit var context: Context
    lateinit var orderModel: OrderModel
    lateinit var businessname: String
    lateinit var businessaddress: String
    lateinit var businessphone: String
     var fontsize: Int = 30

    init {
        context = mcontext;
        orderModel = morderModel;
        this.businessname = businessname
        this.businessaddress = businessaddress
        this.businessphone = businessphone
        this.fontsize = fontsize
    }

    // bluetooth print
    fun bluetoothimageprint(name: String, address: String)   {
        Printooth.init(context)
        if (Printooth.hasPairedPrinter()){
            var printer1 = Printooth.getPairedPrinter()
            Printooth.setPrinter(printer1!!.name, printer1.address)
        }else{
            Printooth.setPrinter(name, address)
        }
        val bitmap: Bitmap =  getBitmapFromView(orderrootget())
        try {
            val options = BitmapCompressOptions()
            Tiny.getInstance().source(bitmap).asBitmap().withOptions(options)
                .compress { isSuccess, bitmap ->
                    if (isSuccess) {
                        var b2 = bitmap
                        var printable: ArrayList<Printable> = ArrayList()
                        printable!!.add(ImagePrintable.Builder(b2).build())
                        Printooth.printer().print(printable)
                    }
                }
        } catch (e: Exception) {

        }



    }


    fun orderrootget(): LinearLayout {
        val printSize: Int = fontsize
        val bind: ViewPrint2Binding = ViewPrint2Binding.inflate(LayoutInflater.from(context))
        if (orderModel.order_channel.uppercase() == "ONLINE") {
            bind.businessName.text = businessname
        } else {
            bind.businessName.setText("")
        }

        var addedDeliveryCharge = 0.0
        bind.businessLocation.text = businessaddress
        bind.businessPhone.text = businessphone
        bind.orderType.text = orderModel.order_type.uppercase()
        bind.orderTime.text = "Order at : " + orderModel.order_date

        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val output: String = formatter.format(parser.parse(orderModel.requested_delivery_timestamp))
        bind.collectionAt.text = "${capitalize(orderModel.order_type)} at : ${output}"
        bind.orderNo.text = "${orderModel.id}";

        var allitemsheight = 0
        bind.items.removeAllViews()
        for (j in 0 until orderModel.order_products.size) {
//            Log.e(orderModel.items.get(j).uuid, orderModel.items.get(j).kitchenItem + " ")
            val childView = getView(j, context, 0, printSize)
            bind.items.addView(childView)
            //                bmps.add(getBitmapFromItemView(childView));
            allitemsheight += childView!!.measuredHeight
        }

        var paidOrNot = "";
        if (orderModel.cash_entry.isNotEmpty()) {
            paidOrNot ="ORDER IS PAID"
        } else if (orderModel.cash_entry.isEmpty()) {
            paidOrNot = "ORDER NOT PAID"
            bind.dueTotalContainer.visibility = View.VISIBLE
            bind.dueTotal.text =
                "£ " + java.lang.String.format(
                    Locale.getDefault(),
                    "%.2f",
                    orderModel.payable_amount
                )
        }

        bind.orderPaidMessage.text = paidOrNot

//        if (orderModel.refundAmount !== 0) {
//            bind.refunded.text =
//                "£ " + java.lang.String.format(Locale.getDefault(), "%.2f", orderModel.refundAmount)
//        } else {
//            bind.refundContainer.visibility = View.GONE
//        }
        bind.refundContainer.visibility = View.GONE

        val subTotal: Double = orderModel.net_amount - addedDeliveryCharge
        bind.subTotal.text =
            "£ " + String.format(Locale.getDefault(), "%.2f", subTotal)

//        if (orderModel.order_type.uppercase(Locale.ROOT).equals("DELIVERY")) {
//            bind.deliveryCharge.text =
//                "£ " + java.lang.String.format(
//                    Locale.getDefault(),
//                    "%.2f",
//                    orderModel.delivery_charge
//                )
//            try {
//                val distance: Double = orderModel.shippingAddress.properties.distance
//                bind.txtDeliveryCharge.text = "Delivery Charge (" + String.format(
//                    Locale.getDefault(),
//                    "%.2f",
//                    distance
//                ) + " miles)"
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            bind.deliveryChargeContainer.visibility = View.GONE
//        }
//        bind.deliveryChargeContainer.visibility = View.GONE
        bind.txtDeliveryCharge.text = "Delivery Charge";
        bind.deliveryCharge.text = "£ " + String.format(Locale.getDefault(), "%.2f", orderModel.delivery_charge.toFloat())
//        bind.deliveryCharge.text =
//                "£ " + String.format(Locale.getDefault(), "%.2f", orderModel.delivery_charge)


//            if (style == 0) {
//                if (cardPayment != 0)
//                    bind.cardPay.setText("£ " + String.format(Locale.getDefault(), "%.2f", cardPayment));
//                else
//                    bind.cardPayContainer.setVisibility(View.GONE);
//
//
//                if (cashPayment != 0)
//                    bind.cashPay.setText("£ " + String.format(Locale.getDefault(), "%.2f", cashPayment));
//                else
//                    bind.cashPayContainer.setVisibility(View.GONE);
//
//            }
        bind.cardPayContainer.visibility = View.GONE
        bind.cashPayContainer.visibility = View.GONE

        if (orderModel.order_channel.uppercase(Locale.getDefault()) == "ONLINE") {
            if (orderModel.discounted_amount > 0) {
//                if (orderModel.discountCode == null) orderModel.discountCode = ""
//                bind.discountTitle.text = "Discount (" + orderModel.discountCode + ")"
                bind.discount.text =
                    "£ " + java.lang.String.format(
                        Locale.getDefault(),
                        "%.2f",
                        orderModel.discounted_amount
                    )
            } else bind.discount.text =
                "£ " + String.format(Locale.getDefault(), "%.2f", 0.0)
        } else {
            var discountStr = "Discount"
//            if (orderModel.d > 0) {
//                discountStr = "Discount (" + orderModel.discountPercentage + "%)"
//            }
            bind.discountTitle.text = discountStr
            var discountAmount = 0.0
//            if (!orderModel.order_channel.equals("ONLINE") && !orderModel.fixedDiscount) {
//                var discountableTotal = 0.0
//                for (item in orderModel.items) {
//                    if (item.uuid.equals("plastic-bag")) {
//                    } else if (item.uuid.equals("container")) {
//                    } else if (item.type.equals("DELIVERY")) {
//                    } else {
//                        var price = 0.0
//                        if (!item.offered) {
//                            price = item.subTotal * item.quantity
//                            if (item.extra != null) {
//                                for (extraItem in item.extra) {
//                                    price += extraItem.price
//                                }
//                            }
//                        } else price = item.total
//                        if (item.discountable) {
//                            discountableTotal += price
//                        } else {
//                            Log.e("Override==>", orderModel.isDiscountOverride + "")
//                            if (orderModel.isDiscountOverride) {
//                                discountableTotal += price
//                            }
//                        }
//                    }
//                }
//                discountAmount = discountableTotal * (orderModel.discountPercentage as Float / 100)
//            } else {
//                discountAmount = orderModel.discountAmount
//            }
            bind.discount.text =
                "£ " + String.format(Locale.getDefault(), "%.2f", orderModel.discounted_amount)
        }

//        if (orderModel.p > 0) bind.plasticBag.text =
//            "£ " + java.lang.String.format(
//                Locale.getDefault(),
//                "%.2f",
//                orderModel.plasticBagCost
//            ) else

        bind.plasticBagContainer.visibility =
            View.GONE


//        if (orderModel.containerBagCost > 0) bind.containerBag.text =
//            "£ " + java.lang.String.format(
//                Locale.getDefault(),
//                "%.2f",
//                orderModel.containerBagCost
//            ) else
        bind.containerBagContainer.visibility =
            View.GONE


//        if (orderModel.adjustmentAmount > 0) bind.adjustment.text =
//            "£ " + java.lang.String.format(
//                Locale.getDefault(),
//                "%.2f",
//                orderModel.adjustmentAmount
//            ) else
        bind.adjustmentContainer.visibility =
            View.GONE

//        if (orderModel.tips > 0) {
//            bind.tipsAmount.text =
//                "£ " + java.lang.String.format(Locale.getDefault(), "%.2f", orderModel.tips)
//        } else {
//            bind.tipsContainer.visibility = View.GONE
//        }
        bind.tipsContainer.visibility = View.GONE

//        if (!orderPrint) {
//            bind.containerOrderNo.visibility = View.GONE
//        } else {
//            bind.containerOrderNo.visibility = View.VISIBLE
//        }
        bind.containerOrderNo.visibility = View.VISIBLE


        bind.total.text =
            "£ " + java.lang.String.format(Locale.getDefault(), "%.2f", orderModel.payable_amount)


        var dlAddress = "Service charge is not included\n\n"



        if (orderModel.order_type == "DELIVERY" || orderModel.order_type == "COLLECTION" || orderModel.order_type == "TAKEOUT") {
            val customerModel: RequesterGuest = orderModel.requester_guest
            if (customerModel != null) {
                dlAddress += "Name : ${customerModel.first_name} ${customerModel.last_name}\n"

                dlAddress += "Phone : ${customerModel.phone}"
            }
        }

        var comment = "Comments : " + orderModel.comment
        comment += """
            
            
            
            
        """.trimIndent()

        bind.comments.text = comment
        bind.address.text = dlAddress

        bind.address.setTextSize(TypedValue.COMPLEX_UNIT_DIP, printSize.toFloat())
        return bind.root;

    }

    fun capitalize(str: String): String? {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }
    private fun getBitmapFromView(view: View): Bitmap {
        val spec = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        view.measure(spec, spec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)

        //create resized image and display
        val maxImageSize = 570f
        val ratio = maxImageSize / returnedBitmap.width
        val width = Math.round(ratio * returnedBitmap.width)
        val height = Math.round(ratio * returnedBitmap.height)
        //return the bitmap
        return Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
    }


    fun getView(position: Int, mCtx: Context?, style: Int, fontSize: Int): View? {
        val binding: ModelPrint2Binding = ModelPrint2Binding.inflate(LayoutInflater.from(mCtx))
        val item: OrderProduct = orderModel.order_products.get(position)
        val str3 = StringBuilder()
        if (position < orderModel.order_products.size - 1) {
            if (orderModel.order_products.get(position).product.sort_order < orderModel.order_products.get(position + 1).product.sort_order) {
                binding.underLine.setVisibility(View.VISIBLE)
            }
        }
        if (style == 0) {
            if (item.components.size > 0) {
                str3.append(item.unit).append(" x ").append(item.product.short_name)
                for (section in item.components) {
                    var _comName = ""
                    if (section.product.short_name.uppercase() != "NONE") {
                        _comName = section.product.short_name
                    }
                    if (section.components.isNotEmpty()) {
                        if (section.components.first().product.short_name.uppercase() != "NONE") {
                            _comName += " -> " + section.components.first().product.short_name
                        }
                    }
                    if (_comName != "") {
                        str3.append("\n").append(_comName)
                    }
                }
            } else {
                str3.append(item.unit).append(" x ").append(item.product.short_name)
            }
        } else {
            if (item.components.size > 0) {
                for (section in item.components) {
                    var _comName = ""
                    if (!section.product.short_name.equals("NONE")) {
                        _comName = section.product.short_name
                    }
                    if (section.product != null) {
                        if (!section.product.short_name.equals("NONE")) _comName += " -> " + section.product.short_name
                    }
                    str3.append(item.unit).append(" x ").append(item.product.short_name).append(" : ")
                        .append(_comName)
                }
            } else {
                str3.append(item.unit).append(" x ").append(item.product.short_name)
            }
        }
//        if (item.extra != null && item.extra.size() > 0) {
//            val str = StringBuilder("\nExtra :")
//            for (extraItem in item.extra) {
//                str.append("  *").append(extraItem.shortName)
//            }
//            str3.append(str.toString())
//        }
        var price = 0.0
//        if (!item.offered) {
//            price = item.subTotal * item.quantity
//            if (item.extra != null) {
//                for (extraItem in item.extra) {
//                    price += extraItem.price
//                }
//            }
//        } else
            price = item.net_amount
        if (item.comment != null) str3.append("\nNote : ").append(item.comment)
        binding.itemText.text = str3.toString()
        binding.itemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        binding.itemPrice.text = "£ " + String.format(Locale.getDefault(), "%.2f", price)
        binding.itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
//        binding.itemPrice.textSize = fontSize.toFloat()
        binding.root.buildDrawingCache(true)
//        Log.e("FontSize==>", binding.itemText.textSize + "f")
//        Log.d("mmmmm", "getView: $position")
        return binding.root
    }


    private fun cutBitmap(h: Int, bitmap: Bitmap): List<Bitmap>? {
        val width = bitmap.width
        val height = bitmap.height
        val full = height % h == 0
        val n = if (height % h == 0) height / h else height / h + 1
        var b: Bitmap
        val bitmaps: MutableList<Bitmap> = java.util.ArrayList()
        for (i in 0 until n) {
            b = if (full) {
                Bitmap.createBitmap(bitmap, 0, i * h, width, h)
            } else {
                if (i == n - 1) {
                    Bitmap.createBitmap(bitmap, 0, i * h, width, height - i * h)
                } else {
                    Bitmap.createBitmap(bitmap, 0, i * h, width, h)
                }
            }
            bitmaps.add(b)
        }
        return bitmaps
    }


}