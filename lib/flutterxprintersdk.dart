import 'package:flutter/services.dart';

import 'Model/printerbusinessmodel.dart';

enum Connectiontype { ip, usb, bluetooth, xbluetooth }

class Flutterxprintersdk {
  final methodChannel = const MethodChannel('flutterxprintersdk');
  String printerconnect = "xprinterconnect";
  String bluetoothprintdata = "bluetoothprintdata";
  String printerdisconect = "printerdisconnect";
  String printimage = "printimage";
  String localorder = "localorder";
  String orderview = "orderview";

  Future<String?> initialxprinter() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future<bool> connectioncheck() async {
    final version = await methodChannel.invokeMethod<bool>('check_connection');
    return version!;
  }

  Future<bool> xprinterconnect(PrinterBusinessModel printermodel) async {
    Map<String, dynamic> quary = {"printer_model_data": printermodel.toJson()};
    final version =
        await methodChannel.invokeMethod<bool>(printerconnect, quary);
    return version!;
  }

  Future bluetoothprint(
      {required Map<String, Object?> orderiteam,
      required PrinterBusinessModel printerBusinessModel}) async {
    Map<String, dynamic> quary = {
      "orderiteam": orderiteam,
      "printer_model_data": printerBusinessModel.toJson()
    };
    await methodChannel.invokeMethod(bluetoothprintdata, quary);
  }

  Future printorder(
      {required Map<String, Object?> orderiteam,
      required PrinterBusinessModel printerBusinessModel}) async {
    Map<String, dynamic> quary = {
      "orderiteam": orderiteam,
      "printer_model_data": printerBusinessModel.toJson()
    };

    return await methodChannel.invokeMethod("print", quary);
  }

  Future<dynamic> xprinterdisconnect() async {
    return await methodChannel.invokeMethod(
      printerdisconect,
    );
  }


  Future<List<int>?> getimagebytes({required Map<String, Object?> orderiteam,required PrinterBusinessModel printerBusinessModel}) async {
    Map<String, dynamic> quary = {"orderiteam": orderiteam, "printer_model_data": printerBusinessModel.toJson()};
    return await methodChannel.invokeMethod<List<int>>(printimage, quary);
  }

  Future<bool> printLocalOrder(
      {required Map<String, Object?> orderiteam,
      required PrinterBusinessModel printerBusinessModel}) async {
    Map<String, dynamic> quary = {
      "orderiteam": orderiteam,
      "printer_model_data": printerBusinessModel.toJson()
    };

    return await methodChannel.invokeMethod(localorder, quary);
  }


  Future getorderview({required Map<String, Object?> orderiteam,
      required PrinterBusinessModel printerBusinessModel}) async {
        Map<String, dynamic> quary = {
      "orderiteam": orderiteam,
      "printer_model_data": printerBusinessModel.toJson()
    };
    await methodChannel.invokeMethod(orderview, quary);
  }
}
