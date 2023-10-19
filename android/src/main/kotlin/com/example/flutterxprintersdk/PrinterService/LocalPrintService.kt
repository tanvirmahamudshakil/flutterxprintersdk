package com.example.flutterxprintersdk.PrinterService

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.example.flutterxprintersdk.Model.LocalOrderDetails.Customer
import com.example.flutterxprintersdk.Model.LocalOrderDetails.LocalOrderDetails
import com.example.flutterxprintersdk.PrinterBusinessData
import com.example.flutterxprintersdk.databinding.ModelPrint2Binding
import com.example.flutterxprintersdk.databinding.ViewPrint2Binding
import com.example.flutterxprintersdk.esepos.OnPrintProcess
import com.example.xprinter.esepos.PosServiceBinding
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.TaskCallback
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class LocalPrintService(mcontext: Context, morderModel: LocalOrderDetails, businessdata: PrinterBusinessData) {
    private var context: Context
    private  var orderModel: LocalOrderDetails
    private  var businessname: String
    private  var businessaddress: String
    private  var businessphone: String
    private var fontsize: Int = 30
    private var noofprint: Int =1
    private var businessdatadata : PrinterBusinessData

    init {
        context = mcontext;
        orderModel = morderModel;
        this.businessname = businessdata.businessname!!;
        this.businessaddress =  businessdata.businessaddress!!;
        this.businessphone =  businessdata.businessphone!!;
        this.fontsize =  businessdata.fontSize!!;
        noofprint = businessdata.printOnCollection!!
        businessdatadata = businessdata;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun orderrootget(): LinearLayout {
        if (orderModel.orderType == "DELIVERY"){
            noofprint = businessdatadata.printOnDelivery!!
        }else if(orderModel.orderType == "COLLECTION"){
            noofprint = businessdatadata.printOnCollection!!
        }else{
            noofprint = businessdatadata.printOnTackwayOrder!!
        }
        val printSize: Int = fontsize
        val bind: ViewPrint2Binding = ViewPrint2Binding.inflate(LayoutInflater.from(context))
        bind.businessName.text = businessname


        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a")

        Log.d("order date", "orderrootget: ${orderModel.orderDate}")
        var addedDeliveryCharge = 0.0
        bind.businessLocation.text = businessaddress
        bind.businessPhone.text = businessphone
        bind.orderType.text = "${orderModel.orderType}"
        bind.orderTime.text = "Order at : ${parser.parse(orderModel.orderDate)
            ?.let { formatter.format(it) }}"
        bind.collectionAt.text = "${orderModel.orderType} at : ${
            orderModel.requestedDeliveryTimestamp?.let {
                parser.parse(it)
                    ?.let { formatter.format(it) }
            }
        }"
        bind.orderNo.text = "${orderModel.localId}";
        var allitemsheight = 0
        bind.items.removeAllViews()
        for (j in orderModel.items!!.indices) {
            val childView = getView(j, context, 0, printSize)
            bind.items.addView(childView)
            allitemsheight += childView!!.measuredHeight
        }
          var paidOrNot = "";
        if (!orderModel.paymentType!!.uppercase().equals("NOTPAY")) {
            paidOrNot ="ORDER IS PAID"
        } else  {
            paidOrNot = "ORDER NOT PAID"
            bind.dueTotalContainer.visibility = View.VISIBLE
            bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
        }
//        paidOrNot = "ORDER NOT PAID"
        bind.dueTotalContainer.visibility = View.VISIBLE
        bind.dueTotal.text = "£ " + String.format("%.2f", (orderModel.payableAmount!! - orderModel.discountedAmount!!) + orderModel.deliveryCharge!!)

        bind.orderPaidMessage.text = paidOrNot
        bind.refundContainer.visibility = View.GONE
        val subTotal: Double = orderModel.netAmount!! - addedDeliveryCharge
        bind.subTotal.text = "£ " + String.format( "%.2f", subTotal)
        bind.txtDeliveryCharge.text = "Delivery Charge";
        bind.deliveryCharge.text = "£ " + orderModel.deliveryCharge!!.toString()
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
                "£ " + String.format( "%.2f", orderModel.discountedAmount!!)
        }
        bind.plasticBagContainer.visibility = View.GONE
        bind.containerBagContainer.visibility = View.GONE
        bind.adjustmentContainer.visibility = View.GONE
        bind.tipsContainer.visibility = View.GONE
        bind.containerOrderNo.visibility = View.VISIBLE
        bind.total.text =
            "£ " +String.format( "%.2f",(orderModel.payableAmount!! - orderModel.discountedAmount!!) + orderModel.deliveryCharge!!)
        var dlAddress = "Service charge is not included\n\n"
        if (orderModel.orderType == "DELIVERY" || orderModel.orderType == "COLLECTION" || orderModel.orderType == "TAKEOUT") {
            val customerModel: Customer? = orderModel.customer
            dlAddress += "Name : ${customerModel!!.firstName} ${customerModel!!.lastName}\n"
            dlAddress += "Phone : ${customerModel.phone}"
        }

        var comment = "Comments : ${if(orderModel.comment != null) orderModel.comment else ""}"

        comment += """




        """.trimIndent()
        bind.comments.text = comment
        bind.address.text = dlAddress
        bind.address.setTextSize(TypedValue.COMPLEX_UNIT_DIP, printSize.toFloat())
        return bind.root;

    }

    @SuppressLint("SetTextI18n")
    fun getView(position: Int, mCtx: Context?, style: Int, fontSize: Int): View? {
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
//            if (item.extra != null) {
//                for (extraItem in item.extra) {
//                    price += extraItem.price!!
//                }
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
    private fun getBitmapFromView(view: View): ArrayList<Bitmap> {
        var bitmaplist : ArrayList<Bitmap>  = ArrayList<Bitmap>();
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
        val width = (ratio * returnedBitmap.width).roundToInt()
        val height = (ratio * returnedBitmap.height).roundToInt()
        //return the bitmap

        var bitmap = Bitmap.createScaledBitmap(returnedBitmap, width, height, true)

        for (i in 1..noofprint){
            bitmaplist.add(bitmap)
        }
        return bitmaplist;
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun printxprinteripdata(serviceBinding: PosServiceBinding) {
        val bitmaplist: ArrayList<Bitmap> =  getBitmapFromView(orderrootget())
        for (bitmap in bitmaplist){
            printBitmap(bitmap, object : OnPrintProcess {
                override fun onSuccess() {
                    Log.d("xprinterdata", "onSuccess: successfully print")
                }

                override fun onError(msg: String?) {
                    Log.d("xprinterdata", "onError: xprinter not print")
                }
            }, serviceBinding)
        }
    }
    fun printBitmap(bitmap: Bitmap?, process: OnPrintProcess, serviceBinding: PosServiceBinding) {
        try {
            val originalBitmap: Bitmap? = bitmap
            val compressFormat = Bitmap.CompressFormat.JPEG
            val compressionQuality = 10 // Adjust the quality as needed

            val compressedData =
                originalBitmap?.let { compressBitmap(it, compressFormat, compressionQuality) }

            var b2 = resizeImage(byteArrayToBitmap(compressedData!!), 550, true)
            printUSBbitamp(b2!!, process, serviceBinding)

        } catch (e: java.lang.Exception) {
            process.onError(e.toString())
        }
    }

    fun resizeImage(bitmap: Bitmap, w: Int, ischecked: Boolean): Bitmap? {
        var resizedBitmap: Bitmap? = null
        val width = bitmap.width
        val height = bitmap.height
        if (width == w) {
            return bitmap
        }

        //resize image with width
        val newHeight = height * w / width
        val scaleWidth = w.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        resizedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        return resizedBitmap
    }

    fun compressBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(format, quality, stream)
        return stream.toByteArray()
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun printUSBbitamp(printBmp: Bitmap, process: OnPrintProcess, serviceBinding: PosServiceBinding) {
        val height = printBmp.height
        // if height > 200 cut the bitmap
        if (height > 200) {
            serviceBinding.binder!!.WriteSendData(object : TaskCallback {
                override fun OnSucceed() {
                    process.onSuccess()
                }

                override fun OnFailed() {
                    process.onError("Failed")
                }
            }, ProcessData {
                val list: MutableList<ByteArray> = java.util.ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                var bitmaplist: List<Bitmap?>? = java.util.ArrayList()
                bitmaplist = cutBitmap(200, printBmp) //cut bitmap
                if (bitmaplist!!.isNotEmpty()) {
                    for (i in bitmaplist.indices) {
                        list.add(
                            DataForSendToPrinterPos80.printRasterBmp(
                                0,
                                bitmaplist[i],
                                BitmapToByteData.BmpType.Threshold,
                                BitmapToByteData.AlignType.Center,
                                576
                            )
                        )
                    }
                }
                list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                list
            })
        } else {
            serviceBinding.binder!!.WriteSendData(object : TaskCallback {
                override fun OnSucceed() {
                    process.onSuccess()
                }

                override fun OnFailed() {
                    process.onError("Failed")
                }
            }, ProcessData {
                val list: MutableList<ByteArray> = java.util.ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                list.add(
                    DataForSendToPrinterPos80.printRasterBmp(
                        0,
                        printBmp,
                        BitmapToByteData.BmpType.Threshold,
                        BitmapToByteData.AlignType.Center,
                        600
                    )
                )
                list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                list
            })
        }
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





    private fun getSingleBitmapFromView(view: View): Bitmap {

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
        val width = (ratio * returnedBitmap.width).roundToInt()
        val height = (ratio * returnedBitmap.height).roundToInt()
        //return the bitmap
        var bitmap = Bitmap.createScaledBitmap(returnedBitmap, width, height, true)
        return bitmap;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getimagebytes(): ByteArray? {
        val newbitmaplist: ArrayList<ByteArray?> = arrayListOf()
        val bitmaplist: Bitmap =  getSingleBitmapFromView(orderrootget())

        var b2 = resizeImage(bitmaplist, 530, true)
        val originalBitmap: Bitmap? = b2
        val compressFormat = Bitmap.CompressFormat.JPEG
        val compressionQuality = 10 // Adjust the quality as needed

           val compressedData =
               originalBitmap?.let { compressBitmap(it, compressFormat, compressionQuality) }


        return compressedData;
    }




    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


}