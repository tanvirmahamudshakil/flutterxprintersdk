// To parse this JSON data, do
//
//     final printerBusinessModel = printerBusinessModelFromJson(jsonString);

import 'dart:convert';

PrinterBusinessModel printerBusinessModelFromJson(String str) => PrinterBusinessModel.fromJson(json.decode(str));

String printerBusinessModelToJson(PrinterBusinessModel data) => json.encode(data.toJson());

class PrinterBusinessModel {
    int? fontSize;
    int? printOnDelivery;
    int? printOnCollection;
    int? printOnTableOrder;
    int? printOnTackwayOrder;
    bool? autoPrint;
    bool? showOrderNoInvoice;
    String? selectPrinter;
    String? printerConnection;
    String? ip;
    String? bluetoothName;
    String? bluetoothAddress;
    String? businessname;
    String? businessphone;
    String? businessaddress;

    PrinterBusinessModel({
        this.fontSize,
        this.printOnDelivery,
        this.printOnCollection,
        this.printOnTableOrder,
        this.printOnTackwayOrder,
        this.autoPrint,
        this.showOrderNoInvoice,
        this.selectPrinter,
        this.printerConnection,
        this.ip,
        this.bluetoothName,
        this.bluetoothAddress,
        this.businessname,
        this.businessphone,
        this.businessaddress,
    });

    factory PrinterBusinessModel.fromJson(Map<String, dynamic> json) => PrinterBusinessModel(
        fontSize: json["font_size"],
        printOnDelivery: json["print_on_delivery"],
        printOnCollection: json["print_on_collection"],
        printOnTableOrder: json["print_on_table_order"],
        printOnTackwayOrder: json["print_on_tackway_order"],
        autoPrint: json["auto_print"],
        showOrderNoInvoice: json["show_order_no_invoice"],
        selectPrinter: json["select_printer"],
        printerConnection: json["printer_connection"],
        ip: json["ip"],
        bluetoothName: json["bluetooth_name"],
        bluetoothAddress: json["bluetooth_address"],
        businessname: json["businessname"],
        businessphone: json["businessphone"],
        businessaddress: json["businessaddress"],
    );

    Map<String, dynamic> toJson() => {
        "font_size": fontSize,
        "print_on_delivery": printOnDelivery,
        "print_on_collection": printOnCollection,
        "print_on_table_order": printOnTableOrder,
        "print_on_tackway_order": printOnTackwayOrder,
        "auto_print": autoPrint,
        "show_order_no_invoice": showOrderNoInvoice,
        "select_printer": selectPrinter,
        "printer_connection": printerConnection,
        "ip": ip,
        "bluetooth_name": bluetoothName,
        "bluetooth_address": bluetoothAddress,
        "businessname": businessname,
        "businessphone": businessphone,
        "businessaddress": businessaddress,
    };
}
