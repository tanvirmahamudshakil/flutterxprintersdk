package com.example.flutterxprintersdk.PrinterService

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.example.flutterxprintersdk.OrderData
import com.example.flutterxprintersdk.PrinterBusinessData
import com.example.flutterxprintersdk.RequesterGuest
import com.example.flutterxprintersdk.databinding.ModelPrint2Binding
import com.example.flutterxprintersdk.databinding.ViewPrint2Binding
import com.example.flutterxprintersdk.esepos.OnPrintProcess
import com.example.xprinter.esepos.PosServiceBinding
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.zxy.tiny.Tiny
import com.zxy.tiny.Tiny.BitmapCompressOptions
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.TaskCallback
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt


class printerservice(mcontext: Context, morderModel: OrderData, businessdata: PrinterBusinessData) {

    private var context: Context
    private  var orderModel: OrderData
    private  var businessname: String
    private  var businessaddress: String
    private  var businessphone: String
    private var fontsize: Int = 30
    private var noofprint: Int =1

    init {
        context = mcontext;
        orderModel = morderModel;
        this.businessname = businessdata.businessname!!;
        this.businessaddress =  businessdata.businessaddress!!;
        this.businessphone =  businessdata.businessphone!!;
        this.fontsize =  businessdata.fontSize!!;
        noofprint = businessdata.printOnCollection!!
    }

    // bluetooth print
    @RequiresApi(Build.VERSION_CODES.O)
    fun bluetoothimageprint(name: String, address: String)   {
        Printooth.init(context)
        if (Printooth.hasPairedPrinter()){
            var printer1 = Printooth.getPairedPrinter()
            Printooth.setPrinter(printer1!!.name, printer1.address)
        }else{
            Printooth.setPrinter(name, address)
        }
        val bitmaplist: ArrayList<Bitmap> =  getBitmapFromView(orderrootget())
        try {
            var printable: ArrayList<Printable> = ArrayList()
            val options = BitmapCompressOptions()
            for (bitmap in bitmaplist){
                Tiny.getInstance().source(bitmap).asBitmap().withOptions(options)
                    .compress { isSuccess, bitmap ->
                        if (isSuccess) {
                            var b2 = bitmap

                            b2 = resizeImage(b2, 530, true)
                            printable!!.add(ImagePrintable.Builder(b2).build())

                        }
                    }
            }
            Printooth.printer().print(printable)
        } catch (e: Exception) {

        }



    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun orderrootget(): LinearLayout {

        val printSize: Int = fontsize
        val bind: ViewPrint2Binding = ViewPrint2Binding.inflate(LayoutInflater.from(context))
        bind.businessName.text = businessname


        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a")


        var addedDeliveryCharge = 0.0
        bind.businessLocation.text = businessaddress
        bind.businessPhone.text = businessphone
        bind.orderType.text = "${orderModel.orderType}"
        bind.orderTime.text = "Order at : ${formatter.format(parser.parse(orderModel.orderDate))}"
        bind.collectionAt.text = "${orderModel.orderType} at : ${formatter.format(parser.parse(orderModel.requestedDeliveryTimestamp))}"

        bind.orderNo.text = "${orderModel.id}";


        var allitemsheight = 0
        bind.items.removeAllViews()
        for (j in orderModel.orderProducts!!.indices) {
            val childView = getView(j, context, 0, printSize)
            bind.items.addView(childView)
            allitemsheight += childView!!.measuredHeight
        }


        var paidOrNot = "";
        if (orderModel.cashEntry!!.isNotEmpty()) {
            paidOrNot ="ORDER IS PAID"
        } else  {
            paidOrNot = "ORDER NOT PAID"
            bind.dueTotalContainer.visibility = View.VISIBLE
            bind.dueTotal.text = "£ " + String.format("%.2f", orderModel.payableAmount)
        }


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
                "£ " + String.format( "%.2f", orderModel.discountedAmount)
        }


        bind.plasticBagContainer.visibility = View.GONE

        bind.containerBagContainer.visibility = View.GONE

        bind.adjustmentContainer.visibility = View.GONE
        bind.tipsContainer.visibility = View.GONE
        bind.containerOrderNo.visibility = View.VISIBLE


        bind.total.text =
            "£ " +String.format( "%.2f",orderModel.payableAmount)


        var dlAddress = "Service charge is not included\n\n"



        if (orderModel.orderType == "DELIVERY" || orderModel.orderType == "COLLECTION" || orderModel.orderType == "TAKEOUT") {
            val customerModel: RequesterGuest? = orderModel.requesterGuest
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

    fun capitalize(str: String): String? {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
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

    fun getView(position: Int, mCtx: Context?, style: Int, fontSize: Int): View? {
        val binding: ModelPrint2Binding = ModelPrint2Binding.inflate(LayoutInflater.from(mCtx))
        val item = orderModel.orderProducts!!.get(position)
        val str3 = StringBuilder()
        if (position < orderModel.orderProducts!!.size - 1) {
            if (orderModel.orderProducts!![position].product!!.sortOrder!! < orderModel.orderProducts!![position + 1].product!!.sortOrder!!) {
                binding.underLine.visibility = View.VISIBLE
            }
        }
        if (style == 0) {
            if (item.components!!.isNotEmpty()) {
                str3.append(item.unit).append(" x ").append(item.product!!.shortName)
                for (section in item.components!!) {
                    var _comName = ""
                    if (section.product!!.shortName!!.uppercase() != "NONE") {
                        _comName = section.product!!.shortName!!
                    }
                    if (section.components!!.isNotEmpty()) {
                        if (section.components.first().product!!.shortName!!.uppercase() != "NONE") {
                            _comName += " -> " + section.components.first().product!!.shortName
                        }
                    }
                    if (_comName != "") {
                        str3.append("\n").append(_comName)
                    }
                }
            } else {
                str3.append(item.unit).append(" x ").append(item.product!!.shortName)
            }
        } else {
            if (item.components!!.isNotEmpty()) {
                for (section in item.components!!) {
                    var _comName = ""
                    if (section.product!!.shortName != "NONE") {
                        _comName = section.product!!.shortName!!
                    }
                    if (section.product!!.shortName != "NONE") _comName += " -> " + section.product!!.shortName
                    str3.append(item.unit).append(" x ").append(item.product!!.shortName).append(" : ")
                        .append(_comName)
                }
            } else {
                str3.append(item.unit).append(" x ").append(item.product!!.shortName)
            }
        }
        var price = 0.0
        price = item.netAmount!!
        if(item.comment != null) str3.append("\nNote : ").append(item.comment)
        binding.itemText.text = str3.toString()
        binding.itemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        binding.itemPrice.text = "£ ${String.format("%.2f", price.toFloat())}"
        binding.itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
        binding.root.buildDrawingCache(true)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun printxprinterusbdata(serviceBinding: PosServiceBinding) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun printxprinterbluetoothdata(serviceBinding: PosServiceBinding) {
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
            val options = BitmapCompressOptions()
            Tiny.getInstance().source(bitmap).asBitmap().withOptions(options)
                .compress { isSuccess, bitmap ->
                    if (isSuccess) {
                        var b2 = bitmap
                        b2 = resizeImage(b2, 530, true)
                        printUSBbitamp(b2, process, serviceBinding)
                    }
                }
        } catch (e: java.lang.Exception) {
            process.onError(e.toString())
        }
    }

    /*
    print the bitmap ,the connection is USB
     */
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

}