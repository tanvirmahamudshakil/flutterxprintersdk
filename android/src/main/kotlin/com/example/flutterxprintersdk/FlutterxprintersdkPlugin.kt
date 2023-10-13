package com.example.flutterxprintersdk

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.flutterxprintersdk.BluetoothPrint.bluetoothprint
import com.example.flutterxprintersdk.PrinterService.printerservice
import com.example.xprinter.esepos.OnDeviceConnect
import com.example.xprinter.esepos.PosServiceBinding
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

/** FlutterxprintersdkPlugin */
class FlutterxprintersdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  private  var checkconnect : String = "check_connection";
  private  var xprinterconnect : String = "xprinterconnect";
  private var printerdisconnect:  String = "printerdisconnect";
  private var bluetoothprintdata: String = "bluetoothprintdata";
  private var print: String = "print";
  private var printimage: String = "printimage";



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
  override fun onMethodCall(call: MethodCall, result: Result) {

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
//      var connecttype = call.argument<String>("type")
//      if(connecttype == "ip"){
//        xprinterconnect(call, result, businessdata);
//      } else if(connecttype == "bluetooth") {
//        bluetooth_printer_connect(call, result, businessdata);
//      } else{
//        xprinterconnect(call, result, businessdata)
//      }
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
    }
    else {
      result.notImplemented()
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

  fun xprinterconnectcheck(call: MethodCall, result: Result) {
    if (serviceBinding.IS_CONNECTED){
      result.success(true)
    }else{
      result.success(false)
    }
  }

  private fun xprinterconnect(call: MethodCall, result: Result,  businessdata: PrinterBusinessData) {
    if (businessdata.printerConnection == "IP Connection"){
      if(!serviceBinding.IS_CONNECTED){
        serviceBinding.connectNet(businessdata.ip.toString(), object : OnDeviceConnect{
          override fun onConnect(isConnect: Boolean) {
            if (isConnect){
              Toast.makeText(context, "connect successfully", Toast.LENGTH_SHORT).show()
              result.success("printer connected successfull")
            }else{
              Toast.makeText(context, "printer not connect. check your printer ip", Toast.LENGTH_SHORT).show()
              result.success("printer not connect. check your printer ip")
            }
          }
        });
      }
    }else if(businessdata.printerConnection == "USB Connection"){
      if(!serviceBinding.IS_CONNECTED){
        serviceBinding.connetUSB(object : OnDeviceConnect{
          override fun onConnect(isConnect: Boolean) {
            if (isConnect){
              Toast.makeText(context, "connect successfully", Toast.LENGTH_SHORT).show()
              result.success("printer connected successfull")
            }else{
              Toast.makeText(context, "printer not connect. check your usb", Toast.LENGTH_SHORT).show()
              result.success("printer not connect. check your printer ip")
            }
          }
        });
      }
    }else{
      if(!serviceBinding.IS_CONNECTED){
        serviceBinding.connetbluetooth(businessdata.bluetoothAddress,object : OnDeviceConnect{
          override fun onConnect(isConnect: Boolean) {
            if (isConnect){
              Toast.makeText(context, "connect successfully", Toast.LENGTH_SHORT).show()
              result.success("printer connected successfull")
            }else{
              Toast.makeText(context, "printer not connect. check your bluetooth address", Toast.LENGTH_SHORT).show()
              result.success("printer not connect. check your bluetooth address")
            }
          }
        });
      }
    }

  }

  private fun  xprinterdisconnect(call: MethodCall, result: Result) {
    if (serviceBinding.IS_CONNECTED){
      serviceBinding.disposeBinding(object : OnDeviceConnect{
        override fun onConnect(isConnect: Boolean) {
          result.success("printer disconnect successfull")
        }
      })
    }else{
      result.success("connect printer not found")
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun xprinterprint(call: MethodCall, result: Result,  businessdata: PrinterBusinessData) {

    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)

    if (serviceBinding.IS_CONNECTED){
      if (businessdata.printerConnection == "IP Connection"){
        printerservice(context,modeldata,businessdata).printxprinteripdata(serviceBinding)
      }else if(businessdata.printerConnection == "USB Connection"){
        printerservice(context,modeldata, businessdata).printxprinterusbdata(serviceBinding)
      }else{
        printerservice(context,modeldata, businessdata).printxprinterbluetoothdata(serviceBinding)
      }
      result.success("print successfull")
    }else{
      result.success("printer not connected")
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
  fun printdata(call: MethodCall, result: Result,  businessdata: PrinterBusinessData) {
    Log.d("xprinter", "printdata: ${businessdata.selectPrinter}")
    if (businessdata.selectPrinter == "X Printer"){

      xprinterprint(call, result,businessdata)
    }else if(businessdata.selectPrinter == "bluetooth") {
      bluetooth_printer_connect(call, result, businessdata);
    }
  }


  @RequiresApi(Build.VERSION_CODES.O)
  fun printimagebytes(call: MethodCall, result: Result, businessdata: PrinterBusinessData) {
    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
     var imagebytesdata =  printerservice(context,modeldata, businessdata).getimagebytes()
    result.success(imagebytesdata)
  }


}
