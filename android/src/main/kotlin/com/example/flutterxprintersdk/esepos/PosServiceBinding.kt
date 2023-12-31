package com.example.xprinter.esepos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.flutterxprintersdk.esepos.OnPrintProcess
import com.example.xprinter.App.TheApp
import com.zxy.tiny.Tiny
import com.zxy.tiny.callback.BitmapCallback
import io.flutter.plugin.common.MethodChannel
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.TaskCallback
import net.posprinter.service.PosprinterService
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80
import net.posprinter.utils.PosPrinterDev
import net.posprinter.utils.PosPrinterDev.PortType
import net.posprinter.utils.StringUtils
import java.io.Serializable

class PosServiceBinding(mcontext: Context)  {
    private var context: Context? = null
    init {
        context = mcontext;
    }
    var IS_CONNECTED = false
    var binder: IMyBinder? = null
    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            //Bind successfully
            binder = iBinder as IMyBinder
            Log.e("binder", "connected")
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("disbinder", "disconnected")
        }
    }

    fun initBinding() {
        val posService = Intent(context, PosprinterService::class.java)
        context!!.bindService(posService, conn, Context.BIND_AUTO_CREATE)
    }

    fun disposeBinding(result: MethodChannel.Result) {

        binder!!.DisconnectCurrentPort(object : TaskCallback {
            override fun OnSucceed() {
                IS_CONNECTED = false;
                result.success(true);
            }
            override fun OnFailed() {
                IS_CONNECTED = true;
                result.success(false);
            }
        })
    }

    fun checkConnection(result: MethodChannel.Result) {
        binder!!.CheckLinkedState(object : TaskCallback {
            override fun OnSucceed() {
                IS_CONNECTED = true;
                result.success(true);
            }

            override fun OnFailed() {
                IS_CONNECTED = false;
                result.success(false);
            }
        })
    }

    fun connectNet(ipAddress: String?, result: MethodChannel.Result) {
        Log.d("tanvir", "xprinter: ${ipAddress}")

        if (ipAddress!! == null || ipAddress == "") {
            result.success(false);
        } else {
            if (binder != null && !TheApp().IS_CONNECT_NET_PRINTER) {
                binder!!.ConnectNetPort(ipAddress, 9100, object : TaskCallback {
                    override fun OnSucceed() {
                        IS_CONNECTED = true
                        result.success(true);
                    }

                    override fun OnFailed() {
                        IS_CONNECTED = false
                        TheApp().IS_CONNECT_NET_PRINTER = false
                        result.success(false);
                    }
                })
            } else {
                Log.e("tanvir binder", "connectNet: binder null", )
                IS_CONNECTED = false
                result.success(false);
            }
        }
    }

    fun connetUSB(result: MethodChannel.Result) {
        val usbList = PosPrinterDev.GetUsbPathNames(context)
        Log.d("usblist", "connetUSB: $usbList")
        if (usbList != null && usbList.size > 0) {
            if (binder != null) {
                binder!!.ConnectUsbPort(context, usbList[0], object : TaskCallback {
                    override fun OnSucceed() {
                        IS_CONNECTED = true
//                        setPortType(PortType.USB)
                        result.success(true);
                    }

                    override fun OnFailed() {
                        IS_CONNECTED = false
                        result.success(false);
                    }
                })
            } else {
                IS_CONNECTED = false
                result.success(false);
                val intent = Intent(TheApp().PRINTER_ALERT_ACTION)
                intent.putExtra(
                    "msg",
                    "Could not initialize printer service!\nPlease, restart the printer.."
                )
                context!!.sendBroadcast(intent)
            }
        } else {
            IS_CONNECTED = false
            result.success(false);
            val intent = Intent(TheApp().PRINTER_ALERT_ACTION)
            intent.putExtra("msg", "Could not connect to printer!")
            context!!.sendBroadcast(intent)
        }
    }
    fun connetbluetooth(printeraddress : String?,result: MethodChannel.Result) {
        Toast.makeText(context, "${printeraddress}", Toast.LENGTH_SHORT).show();
        if (printeraddress != null && printeraddress == "") {
            if (binder != null) {
                binder!!.ConnectBtPort(printeraddress, object : TaskCallback {
                    override fun OnSucceed() {
                        IS_CONNECTED = true
                        result.success(true)
                    }
                    override fun OnFailed() {
                        IS_CONNECTED = false
                        result.success(false)
                    }
                })
            } else {
                IS_CONNECTED = false
                result.success(false)
                val intent = Intent(TheApp().PRINTER_ALERT_ACTION)
                intent.putExtra(
                    "msg",
                    "Could not initialize printer service!\nPlease, restart the printer.."
                )
                context!!.sendBroadcast(intent)
            }
        } else {
            IS_CONNECTED = false
            result.success(false)
            val intent = Intent(TheApp().PRINTER_ALERT_ACTION)
            intent.putExtra("msg", "Could not connect to printer!")
            context!!.sendBroadcast(intent)
        }
    }

    fun printText(text: String?, process: OnPrintProcess) {
        binder!!.WriteSendData(object : TaskCallback {
            override fun OnSucceed() {
                process.onSuccess()
            }

            override fun OnFailed() {
                process.onError("Print failed")
            }
        }, ProcessData {
            val list: MutableList<ByteArray> = ArrayList()
            if (text!! == null || text == "") {
                process.onError("Printing text is empty!")
            } else {
                list.add(DataForSendToPrinterPos80.initializePrinter())
                val data1: ByteArray = StringUtils.strTobytes(text)
                list.add(data1)
                //should add the command of print and feed line,because print only when one line is complete, not one line, no print
                list.add(DataForSendToPrinterPos80.printAndFeedLine())
                //cut pager
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                return@ProcessData list
            }
            null
        })
    }

    fun printBitmap(bitmap: Bitmap?, process: OnPrintProcess) {
        try {
            val options: Tiny.BitmapCompressOptions = Tiny.BitmapCompressOptions()
            Tiny.getInstance().source(bitmap).asBitmap().withOptions(options)
                .compress(object : BitmapCallback {
                    override fun callback(isSuccess: Boolean, bitmap: Bitmap?) {
                        if (isSuccess) {
                            var b2 = bitmap
                            b2 = resizeImage(b2, 530, true)
                            printUSBbitamp(b2, process)
                        }
                    }
                })
        } catch (e: Exception) {
            process.onError(e.toString())
        }
    }

    /*
    print the bitmap ,the connection is USB
     */
    private fun printUSBbitamp(printBmp: Bitmap?, process: OnPrintProcess) {
        val height = printBmp!!.height
        // if height > 200 cut the bitmap
        if (height > 200) {
            binder!!.WriteSendData(object : TaskCallback {
                override fun OnSucceed() {
                    process.onSuccess()
                }

                override fun OnFailed() {
                    process.onError("Failed")
                }
            }) {
                val list: MutableList<ByteArray> =
                    ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                var bitmaplist: List<Bitmap?> =
                    ArrayList()
                bitmaplist = cutBitmap(200, printBmp) //cut bitmap
                if (bitmaplist.size != 0) {
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
            }
        } else {
            binder!!.WriteSendData(object : TaskCallback {
                override fun OnSucceed() {
                    process.onSuccess()
                }

                override fun OnFailed() {
                    process.onError("Failed")
                }
            }) {
                val list: MutableList<ByteArray> =
                    ArrayList()
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
            }
        }
    }

    private fun cutBitmap(h: Int, bitmap: Bitmap?): List<Bitmap?> {
        val width = bitmap!!.width
        val height = bitmap.height
        val full = height % h == 0
        val n = if (height % h == 0) height / h else height / h + 1
        var b: Bitmap?
        val bitmaps: MutableList<Bitmap?> = ArrayList()
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

    fun resizeImage(bitmap: Bitmap?, w: Int, ischecked: Boolean): Bitmap? {
        var resizedBitmap: Bitmap? = null
        val width = bitmap!!.width
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
        resizedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            width,
            height,
            matrix,
            true
        )
        return resizedBitmap
    }

    var portType //connect type
            : PortType? = null
//
//    private fun setPortType(portType: PortType) {
//        this.portType = portType
//    }
}