package com.example.flutterxprintersdk

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.flutterxprintersdk.BluetoothPrint.bluetoothprint
import com.example.flutterxprintersdk.Model.OrderModel.OrderModel
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

/** FlutterxprintersdkPlugin */
class FlutterxprintersdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  private  var checkconnect : String = "check_connection";
  private  var printerconnect : String = "printerconnect";
  private var printerdisconnect:  String = "printerdisconnect";
  private var bluetoothprintdata: String = "bluetoothprintdata";
  private var print: String = "print";


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

    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      xprinterinit();
    }
    else if(call.method == checkconnect){
      xprinterconnectcheck(call, result);
    }
    else if(call.method == printerconnect) {
      var connecttype = call.argument<String>("type")
      if(connecttype == "ip"){
        xprinter_connect_ip(call, result);
      } else if(connecttype == "bluetooth") {
        bluetooth_printer_connect(call, result);
      } else{
        xprinter_connect_usb(call, result)
      }

    }
    else if(call.method == bluetoothprintdata) {
      bluetooth_print_data(call, result);
    } else if(call.method == print){
      printdata(call, result)
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
    serviceBinding.checkConnection(object : OnDeviceConnect{
      override fun onConnect(isConnect: Boolean) {
        Toast.makeText(context, "connected ${isConnect}", Toast.LENGTH_SHORT).show();
        result.success(isConnect)
      }
    })
  }

  fun xprinter_connect_ip(call: MethodCall, result: Result) {
    var connecttype = call.argument<String>("type")
    var ip: String? = call.argument<String>("ip")
    serviceBinding.connectNet(ip, object : OnDeviceConnect{
      override fun onConnect(isConnect: Boolean) {
        if (isConnect == true){
          result.success("printer connected successfull")
        }else{
          result.success("printer not connect. check your printer ip")
        }
      }
    });
  }

  fun xprinter_connect_usb(call: MethodCall, result: Result) {
    serviceBinding.connetUSB(object : OnDeviceConnect{
      override fun onConnect(isConnect: Boolean) {
        if (isConnect == true){
          result.success("printer connected successfull")
        }else{
          result.success("printer not connect. check your usb cable")
        }
      }
    })
  }

  fun bluetooth_printer_connect(call: MethodCall, result: Result){
    var connecttype = call.argument<String>("type")
    var bluetoothname: String? = call.argument<String>("bluetoothname")
    var bluetoothaddress: String? = call.argument<String>("bluetoothaddress")
    var businessname : String? = call.argument("businessname")
    var businesssaddress : String? = call.argument("businessaddress")
    var businessphone : String? = call.argument("businessphone")
    var fontsize : Int? = call.argument("fontsize")
    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderModel>(json, OrderModel::class.java)
//    bluetoothprint(context).bluetoothprinterinit();
//    bluetoothprint(context).bluetoothconnect(bluetoothname!!, bluetoothaddress!!)
    printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).bluetoothimageprint(bluetoothname!!, bluetoothaddress!!)
  }


  fun bluetooth_print_data(call: MethodCall, result: Result) {
    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderModel>(json, OrderModel::class.java)
    bluetoothprint(context).orderprint(modeldata)

  }


  fun printdata(call: MethodCall, result: Result) {
    var connecttype = call.argument<String>("type")
    if (connecttype == "ip"){
      xprinter_connect_ip(call, result)
    }else if(connecttype == "usb") {
      xprinter_connect_usb(call, result)
    }else if(connecttype == "bluetooth") {

      bluetooth_printer_connect(call, result);
    }
  }
}
