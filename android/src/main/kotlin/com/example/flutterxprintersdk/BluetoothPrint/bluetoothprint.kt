package com.example.flutterxprintersdk.BluetoothPrint

import android.content.Context
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.flutterxprintersdk.Model.OrderModel.OrderModel
import com.example.flutterxprintersdk.PrinterService.printerservice
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity

class bluetoothprint(mcontext : Context) {

    var context : Context;

    init {
        context = mcontext;
    }

    fun bluetoothprinterinit() {
        Printooth.init(context)

    }

    fun  bluetoothconnect(name: String, address: String) {

        if (Printooth.hasPairedPrinter()){
            var printer1 = Printooth.getPairedPrinter()
            Printooth.setPrinter(printer1!!.name, printer1.address)
//            printerservice().bluetoothimageprint(context);
        }else{
            Printooth.setPrinter(name, address)
        }
    }

   fun disconnected() {
       Printooth.removeCurrentPrinter()
   }


    fun orderprint( morderModel: OrderModel) {
//        printerservice(context, morderModel).bluetoothimageprint()
    }


    fun printtext() {
        var printables = ArrayList<Printable>()
        var printable = TextPrintable.Builder()
            .setText("£ 0 Hello World") //The text you want to print
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD) //Bold or normal
            .setFontSize(DefaultPrinter.FONT_SIZE_NORMAL)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON) // Underline on/off
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252) // Character code to support languages
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setNewLinesAfter(1) // To provide n lines after sentence
            .build()
        
        printables.add(printable)

        Printooth.printer().print(printables)
//        printerservice().imageprint(context)
    }


}