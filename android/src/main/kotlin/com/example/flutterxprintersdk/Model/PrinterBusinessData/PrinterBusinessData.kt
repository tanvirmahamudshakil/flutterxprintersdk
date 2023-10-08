package com.example.flutterxprintersdk

import com.google.gson.annotations.SerializedName


data class PrinterBusinessData (

  @SerializedName("font_size"              ) var fontSize            : Int?     = null,
  @SerializedName("print_on_delivery"      ) var printOnDelivery     : Int?     = null,
  @SerializedName("print_on_collection"    ) var printOnCollection   : Int?     = null,
  @SerializedName("print_on_table_order"   ) var printOnTableOrder   : Int?     = null,
  @SerializedName("print_on_tackway_order" ) var printOnTackwayOrder : Int?     = null,
  @SerializedName("auto_print"             ) var autoPrint           : Boolean? = null,
  @SerializedName("show_order_no_invoice"  ) var showOrderNoInvoice  : Boolean? = null,
  @SerializedName("select_printer"         ) var selectPrinter       : String?  = null,
  @SerializedName("printer_connection"     ) var printerConnection   : String?  = null,
  @SerializedName("ip"                     ) var ip                  : String?  = null,
  @SerializedName("bluetooth_name"         ) var bluetoothName       : String?  = null,
  @SerializedName("bluetooth_address"      ) var bluetoothAddress    : String?  = null,
  @SerializedName("businessname"           ) var businessname        : String?  = null,
  @SerializedName("businessphone"          ) var businessphone       : String?  = null

)