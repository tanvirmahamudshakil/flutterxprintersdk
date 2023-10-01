import 'package:flutter/services.dart';

enum Connectiontype { ip, usb, bluetooth }

class Flutterxprintersdk {
  final methodChannel = const MethodChannel('flutterxprintersdk');
  String printerconnect = "xprinterconnect";
  String bluetoothprintdata = "bluetoothprintdata";
  String printerdisconect = "printerdisconnect";

  Future<String?> initialxprinter() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future<bool> connectioncheck() async {
    final version =
        await methodChannel.invokeMethod<bool>('check_connection', []);
    return version!;
  }

  Future<String> xprinterconnect(
      Connectiontype connectiontype, String? ip) async {
    Map<String, dynamic> data = {
      "type": connectiontype == Connectiontype.ip
          ? "ip"
          : connectiontype == Connectiontype.bluetooth
              ? "bluetooth"
              : "usb",
      "ip": ip
    };
    final version =
        await methodChannel.invokeMethod<String>(printerconnect, data);
    return version!;
  }

  Future bluetoothprint(Map<String, Object?> orderiteam) async {
        await methodChannel.invokeMethod(bluetoothprintdata, {"orderiteam" : orderiteam});
  }

  Future<dynamic> printorder({required Map<String, Object?> orderiteam, required Connectiontype connectiontype, String? ip, String? bluetoothname, String? bluetoothaddress, required String businessname, required String businessaddress, required String businessphone, required int fontsize}) async {
    Map<String, dynamic> quary = {
      "orderiteam": orderiteam, 
      "type": connectiontype == Connectiontype.ip
          ? "ip"
          : connectiontype == Connectiontype.bluetooth
              ? "bluetooth"
              : "usb",
      "ip": ip, 
      "bluetoothname": bluetoothname, 
      "bluetoothaddress": bluetoothaddress,
      "businessname": businessname,
      "businessaddress": businessaddress,
      "businessphone": businessphone,
      "fontsize": fontsize
    };

   return await methodChannel.invokeMethod("print",quary);
  }

  Future<dynamic> xprinterdisconnect() async {
    return await methodChannel.invokeMethod(printerdisconect);
  }

}
