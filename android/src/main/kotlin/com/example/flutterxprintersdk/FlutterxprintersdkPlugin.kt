package com.example.flutterxprintersdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

import com.example.flutterxprintersdk.BluetoothPrint.bluetoothprint
import com.example.flutterxprintersdk.Model.LocalOrderDetails.LocalOrderDetails
import com.example.flutterxprintersdk.PrinterService.LocalPrintService
import com.example.flutterxprintersdk.PrinterService.printerservice
import com.example.xprinter.esepos.OnDeviceConnect
import com.example.xprinter.esepos.PosServiceBinding
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/** FlutterxprintersdkPlugin */


  class FlutterxprintersdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

    private  var checkconnect : String = "check_connection";
    private  var xprinterconnect : String = "xprinterconnect";
    private var printerdisconnect:  String = "printerdisconnect";
    private var bluetoothprintdata: String = "bluetoothprintdata";
    private var print: String = "print";
    private var printimage: String = "printimage";
    private var localorder: String = "localorder";
    private var orderview: String = "orderview"



    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel : MethodChannel
    private lateinit var context : Context
    private lateinit var activity : Activity

    lateinit var serviceBinding: PosServiceBinding

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
      channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutterxprintersdk")
      context = flutterPluginBinding.applicationContext
      serviceBinding = PosServiceBinding(context)
      serviceBinding.initBinding()
      bluetoothprint(context).bluetoothprinterinit();
      channel.setMethodCallHandler(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override  fun onMethodCall(call: MethodCall, result: Result) {

     CoroutineScope(Dispatchers.IO).launch {
       var printerbusinessdata = call.argument<Map<String, Any>>("printer_model_data")

       val json = Gson().toJson(printerbusinessdata)
       Log.d("printer data", "onMethodCall: ${json}")
       var businessdata = Gson().fromJson<PrinterBusinessData>(json, PrinterBusinessData::class.java)
       if (call.method == "getPlatformVersion") {
         xprinterinit();
       }
       else if(call.method == checkconnect){
         xprinterconnectcheck(call, result);
       }
       else if(call.method == xprinterconnect) {
         xprinterconnect(call, result, businessdata)
       }
       else if(call.method == bluetoothprintdata) {
         bluetooth_print_data(call, result, businessdata);
       } else if(call.method == print){
         printdata(call, result, businessdata)
       }else if(call.method == printerdisconnect) {
         xprinterdisconnect(call, result);
       }else if(call.method == printimage) {
         printimagebytes(call, result, businessdata)
       }else if(call.method == localorder) {
         printdata(call, result, businessdata, true)
       }else if(call.method == orderview) {
         orderactivity(call, result, businessdata)
       }
       else {
         result.notImplemented()
       }
     }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
      channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
      activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

    }

    override fun onDetachedFromActivity() {

    }

    fun xprinterinit() {
      serviceBinding.initBinding()
    }

    private fun xprinterconnectcheck(call: MethodCall, result: Result) {
      serviceBinding.checkConnection(result)
    }

    private fun xprinterconnect(call: MethodCall, result: Result,  businessdata: PrinterBusinessData) {
      if (businessdata.printerConnection == "IP Connection"){
        serviceBinding.connectNet(businessdata.ip.toString(),result);
      }else if(businessdata.printerConnection == "USB Connection"){
        serviceBinding.connetUSB(result);
      }else{
        serviceBinding.connetbluetooth(businessdata.bluetoothAddress,result);
      }
    }

    private fun  xprinterdisconnect(call: MethodCall, result: Result) {
      if (serviceBinding.IS_CONNECTED){
        serviceBinding.disposeBinding(result)
      }else{
        result.success("connect printer not found")
      }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun xprinterprint(call: MethodCall, result: Result, businessdata: PrinterBusinessData, local: Boolean = false) {
      if (local){
     var model =   CoroutineScope(Dispatchers.IO).async {
          var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
          val json = Gson().toJson(orderiteamdata)
          var modeldata = Gson().fromJson<LocalOrderDetails>(json, LocalOrderDetails::class.java)
          modeldata;
        }
        if (businessdata.printerConnection == "IP Connection"){
          LocalPrintService(context,model.await(),businessdata).printxprinteripdata(serviceBinding, result)
        }else if(businessdata.printerConnection == "USB Connection"){
          LocalPrintService(context,model.await(), businessdata).printxprinteripdata(serviceBinding, result)
        }else{
//       printerservice(context,modeldata, businessdata).printxprinterbluetoothdata(serviceBinding)
//       bluetoothprint(context).bluetoothconnect(businessdata.bluetoothName!!, businessdata.bluetoothAddress!!)
//       printerservice(context, modeldata,businessdata).bluetoothimageprint(businessdata.bluetoothName!!, businessdata.bluetoothAddress!!)
        }

      }else{
        var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
        val json = Gson().toJson(orderiteamdata)
        Log.d("json data", "xprinterprint: ${json}")
        var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
        if (businessdata.printerConnection == "IP Connection"){
          printerservice(context,modeldata,businessdata).printxprinteripdata(serviceBinding, result)
        }else if(businessdata.printerConnection == "USB Connection"){
          printerservice(context,modeldata, businessdata).printxprinterusbdata(serviceBinding, result)

        }else{
          bluetoothprint(context).bluetoothconnect(businessdata.bluetoothName!!, businessdata.bluetoothAddress!!)
          printerservice(context, modeldata,businessdata).bluetoothimageprint(businessdata.bluetoothName!!, businessdata.bluetoothAddress!!)
        }
      }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun bluetooth_printer_connect(call: MethodCall, result: Result,  businessdata: PrinterBusinessData){

      var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
      val json = Gson().toJson(orderiteamdata)
      var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)

      bluetoothprint(context).bluetoothconnect(businessdata.bluetoothName!!, businessdata.bluetoothAddress!!)
      printerservice(context, modeldata,businessdata).bluetoothimageprint(businessdata.bluetoothName!!, businessdata.bluetoothAddress!!)

    }


    private fun bluetooth_print_data(call: MethodCall, result: Result,  businessdata: PrinterBusinessData) {
      var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
      val json = Gson().toJson(orderiteamdata)
      var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
      bluetoothprint(context).orderprint(modeldata)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun printdata(call: MethodCall, result: Result, businessdata: PrinterBusinessData, local : Boolean = false) {
      Log.d("xprinter", "printdata: ${businessdata.selectPrinter}")
      if (businessdata.selectPrinter == "X Printer"){
        Log.d("xprinterdata", "onMethodCall: loacal ${Boolean}")
        xprinterprint(call, result,businessdata,local)
      }else if(businessdata.selectPrinter == "bluetooth") {
        bluetooth_printer_connect(call, result, businessdata);
      }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun printimagebytes(call: MethodCall, result: Result, businessdata: PrinterBusinessData) {
      var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
      val json = Gson().toJson(orderiteamdata)
      var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
      var imagebytesdata: ByteArray =  printerservice(context,modeldata, businessdata).getimagebytes()
      result.success(imagebytesdata)
    }

    fun orderactivity(call: MethodCall, result: Result, businessdata: PrinterBusinessData) {
      val intent = Intent(context, OrderView::class.java)
      var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
      val json = Gson().toJson(orderiteamdata)

      var printerbusinessdata = call.argument<Map<String, Any>>("printer_model_data")

      val business = Gson().toJson(printerbusinessdata)
      intent.putExtra("orderiteam", json);
      intent.putExtra("business", business);
      activity.startActivity(intent)
    }




  }