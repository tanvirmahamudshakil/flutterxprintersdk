import 'package:flutter/services.dart';
import 'package:flutter_esc_pos_utils/flutter_esc_pos_utils.dart';
import 'package:flutter_pos_printer_platform_image_3/flutter_pos_printer_platform_image_3.dart';
import 'package:flutterxprintersdk/Model/printerbusinessmodel.dart';
import 'package:flutterxprintersdk/flutterxprintersdk.dart';
import 'package:flutterxprintersdk_example/json.dart';
import 'package:image/image.dart';

class ImagePrintData {
  final _flutterxprintersdkPlugin = Flutterxprintersdk();

  Future imageprint() async {
     var printerManager = PrinterManager.instance;
       final profile = await CapabilityProfile.load();
      final generator = Generator(PaperSize.mm80, profile);
      List<int> bytes = [];
       var data = await _flutterxprintersdkPlugin.getimagebytes(
                      orderiteam: localorderjson,
                      printerBusinessModel: printermodel);
                
     
   
    final Image image = decodeImage(Uint8List.fromList(data!))!;
    bytes += generator.image(image);
    bytes+= generator.feed(2);
    bytes+= generator.cut();
     printerManager
          .discovery(type: PrinterType.usb, isBle: false)
          .listen((device) async {
        print("hjbjbcjahs ${device.name}");
       var conn =  await printerManager.connect(
            type: PrinterType.usb,
            model: UsbPrinterInput(
                name: device.name,
                productId: device.productId,
                vendorId: device.vendorId));
                if (conn) {
                   printerManager.send(type: PrinterType.usb, bytes: bytes);
                }
      });
  }

  PrinterBusinessModel printermodel = PrinterBusinessModel(
      autoPrint: true,
      fontSize: 16,
      printOnCollection: 1,
      printOnDelivery: 1,
      printOnTableOrder: 1,
      printOnTackwayOrder: 1,
      printerConnection: "USB Connection",
      selectPrinter: "X Printer",
      showOrderNoInvoice: true,
      bluetoothAddress: "DC:0D:30:EE:30:2A",
      bluetoothName: "dsvsdvsd",
      businessname: "sdvsdvsdv",
      businessphone: "01932336565",
      ip: "192.168.0.104",
      businessaddress: "sdkjbvjsdhbvjhsbdv");

}