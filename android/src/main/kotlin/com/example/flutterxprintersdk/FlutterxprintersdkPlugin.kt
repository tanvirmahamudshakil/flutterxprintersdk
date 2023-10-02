package com.example.flutterxprintersdk

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.flutterxprintersdk.BluetoothPrint.bluetoothprint
import com.example.flutterxprintersdk.Model.OrderModel2.OrderData
import com.example.flutterxprintersdk.PrinterService.printerservice
import com.example.flutterxprintersdk.esepos.OnPrintProcess
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

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      xprinterinit();
    }
    else if(call.method == checkconnect){
      xprinterconnectcheck(call, result);
    }
    else if(call.method == xprinterconnect) {
      var connecttype = call.argument<String>("type")
      if(connecttype == "ip"){
        xprinterconnect(call, result);
      } else if(connecttype == "bluetooth") {
        bluetooth_printer_connect(call, result);
      } else{
        xprinterconnect(call, result)
      }
    }
    else if(call.method == bluetoothprintdata) {
      bluetooth_print_data(call, result);
    } else if(call.method == print){
      printdata(call, result)
    }else if(call.method == printerdisconnect) {
      xprinterdisconnect(call, result);
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

  fun xprinterconnect(call: MethodCall, result: Result) {
    var connecttype = call.argument<String>("type")
    var ip: String? = call.argument<String>("ip")
    if (connecttype == "ip"){
      if(!serviceBinding.IS_CONNECTED){
        serviceBinding.connectNet(ip, object : OnDeviceConnect{
          override fun onConnect(isConnect: Boolean) {
            Log.d("connection", "onConnect: ${isConnect}")
            if (isConnect){
              Toast.makeText(context, "connect successfully", Toast.LENGTH_SHORT).show()
              //       printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinteripdata(serviceBinding)
              result.success("printer connected successfull")
            }else{
              Toast.makeText(context, "printer not connect. check your printer ip", Toast.LENGTH_SHORT).show()
              result.success("printer not connect. check your printer ip")
            }
          }
        });
      }
    }else{
      if(!serviceBinding.IS_CONNECTED){
        serviceBinding.connetUSB(object : OnDeviceConnect{
          override fun onConnect(isConnect: Boolean) {
            if (isConnect){
              Toast.makeText(context, "connect successfully", Toast.LENGTH_SHORT).show()
              //       printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinteripdata(serviceBinding)

              result.success("printer connected successfull")
            }else{
              Toast.makeText(context, "printer not connect. check your usb", Toast.LENGTH_SHORT).show()
              result.success("printer not connect. check your printer ip")
            }
          }
        });
      }
    }

  }

  fun  xprinterdisconnect(call: MethodCall, result: Result) {
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
  fun xprinterprint(call: MethodCall, result: Result) {
    var connecttype = call.argument<String>("type")
    var ip: String? = call.argument<String>("ip")
    var bluetoothname: String? = call.argument<String>("bluetoothname")
    var bluetoothaddress: String? = call.argument<String>("bluetoothaddress")
    var businessname : String? = call.argument("businessname")
    var businesssaddress : String? = call.argument("businessaddress")
    var businessphone : String? = call.argument("businessphone")
    var fontsize : Int? = call.argument("fontsize")
    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)

    if (serviceBinding.IS_CONNECTED){
      if (connecttype == "ip"){
        printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinteripdata(serviceBinding)
      }else{
        printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinterusbdata(serviceBinding)
      }
      result.success("print successfull")
    }else{
      result.success("printer not connected")
    }

  }

//  fun xprinter_connect_ip(call: MethodCall, result: Result) {
//    var connecttype = call.argument<String>("type")
//    var ip: String? = call.argument<String>("ip")
//    var bluetoothname: String? = call.argument<String>("bluetoothname")
//    var bluetoothaddress: String? = call.argument<String>("bluetoothaddress")
//    var businessname : String? = call.argument("businessname")
//    var businesssaddress : String? = call.argument("businessaddress")
//    var businessphone : String? = call.argument("businessphone")
//    var fontsize : Int? = call.argument("fontsize")
//    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
//    val json = Gson().toJson(orderiteamdata)
//    var modeldata = Gson().fromJson<OrderModel>(json, OrderModel::class.java)
//    if (serviceBinding.IS_CONNECTED){
//      CoroutineScope(Dispatchers.IO).async {
//        serviceBinding.disposeBinding(object : OnDeviceConnect{
//          override fun onConnect(isConnect: Boolean) {
//            if (!isConnect) {
//              Toast.makeText(context, "disconnect successfully", Toast.LENGTH_SHORT).show()
//            }
//          }
//
//        });
//      }
//      serviceBinding.connectNet(ip, object : OnDeviceConnect{
//        override fun onConnect(isConnect: Boolean) {
//          Log.d("connection", "onConnect: ${isConnect}")
//          if (isConnect){
//
//            //       printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinteripdata(serviceBinding)
//            result.success("printer connected successfull")
//          }else{
//            result.success("printer not connect. check your printer ip")
//          }
//        }
//      });
//
//
//
////      Toast.makeText(context, "all ready connected connected", Toast.LENGTH_SHORT).show()
//    }else{
//
//      Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show()
//      serviceBinding.connectNet(ip, object : OnDeviceConnect{
//        override fun onConnect(isConnect: Boolean) {
//          Log.d("connection", "onConnect: ${isConnect}")
//          if (isConnect){
//
//   //       printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinteripdata(serviceBinding)
//            result.success("printer connected successfull")
//          }else{
//            result.success("printer not connect. check your printer ip")
//          }
//        }
//      });
//
//    }
//
//
//
//
//
//
//
//  }

  fun xprinter_connect_usb(call: MethodCall, result: Result) {
    Log.d("xprinter", "xprinter_connect_usb: usb connect")
    var connecttype = call.argument<String>("type")
    var ip: String? = call.argument<String>("ip")
    var bluetoothname: String? = call.argument<String>("bluetoothname")
    var bluetoothaddress: String? = call.argument<String>("bluetoothaddress")
    var businessname : String? = call.argument("businessname")
    var businesssaddress : String? = call.argument("businessaddress")
    var businessphone : String? = call.argument("businessphone")
    var fontsize : Int? = call.argument("fontsize")
    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
    serviceBinding.connetUSB(object : OnDeviceConnect{
      @RequiresApi(Build.VERSION_CODES.O)
      override fun onConnect(isConnect: Boolean) {
        if (isConnect == true){
          printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).printxprinterusbdata(serviceBinding)
          result.success("printer connected successfull")
        }else{
          result.success("printer not connect. check your usb cable")
        }
      }
    })
  }

  @RequiresApi(Build.VERSION_CODES.O)
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
    var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
    bluetoothprint(context).bluetoothprinterinit();
    bluetoothprint(context).bluetoothconnect(bluetoothname!!, bluetoothaddress!!)
    printerservice(context,modeldata, businessname!!,businesssaddress!!,fontsize!!, businessphone!!).bluetoothimageprint(bluetoothname!!, bluetoothaddress!!)


  }


  fun bluetooth_print_data(call: MethodCall, result: Result) {
    var orderiteamdata = call.argument<Map<String, Any>>("orderiteam")
    val json = Gson().toJson(orderiteamdata)
    var modeldata = Gson().fromJson<OrderData>(json, OrderData::class.java)
    bluetoothprint(context).orderprint(modeldata)

  }


  @RequiresApi(Build.VERSION_CODES.O)
  fun printdata(call: MethodCall, result: Result) {
    var connecttype = call.argument<String>("type")
    if (connecttype == "ip"){
      xprinterprint(call, result)
    }else if(connecttype == "usb") {
      xprinterprint(call, result)
    }else if(connecttype == "bluetooth") {
      bluetooth_printer_connect(call, result);
    }
  }
}
