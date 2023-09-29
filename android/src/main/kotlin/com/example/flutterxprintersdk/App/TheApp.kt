package com.example.xprinter.App

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.xprinter.esepos.PosServiceBinding

class TheApp {

    var DeviceSN  = ""
    var DeviceVer:kotlin.String? = ""
    var deviceConnect = ""
    var canceled = false

    var serviceBinding: PosServiceBinding? = null
     var IS_CONNECT_NET_PRINTER = false
    var IS_CONNECT_USB_PRINTER = false

    val ORDER_ALERT_ACTION_PLAY = "ORDER_ALERT_INTENT_ACTION_PLAY"
    val ORDER_ALERT_ACTION_STOP = "ORDER_ALERT_INTENT_ACTION_STOP"
    val PRINTER_ALERT_ACTION = "PRINTER_ALERT_INTENT_ACTION"

    fun initialposprinter(context: Context) {
//        serviceBinding = PosServiceBinding(context)
//        serviceBinding!!.initBinding(context)
    }



}