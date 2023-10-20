import 'dart:developer';
import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutter_pos_printer_platform_image_3/flutter_pos_printer_platform_image_3.dart';
import 'package:flutterxprintersdk/Model/printerbusinessmodel.dart';
import 'package:flutterxprintersdk/flutterxprintersdk.dart';
import 'package:flutterxprintersdk_example/image_print.dart';
import 'package:image/image.dart';
import 'package:permission_handler/permission_handler.dart';
import 'json.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _flutterxprintersdkPlugin = Flutterxprintersdk();

  @override
  void initState() {
    scandevice();
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    platformVersion = await _flutterxprintersdkPlugin.initialxprinter() ??
        'Unknown platform version';
  }

  Future connectioncheck() async {
    var data = await _flutterxprintersdkPlugin.connectioncheck();
    print(data);
  }

  Connectiontype connectiontype = Connectiontype.bluetooth;

  Future<void> printerconnect() async {
    var data = await _flutterxprintersdkPlugin.xprinterconnect(printermodel);
    print(data);
  }

  Future scandevice() async {
    if (Platform.isAndroid) {
      [
        Permission.location,
        Permission.storage,
        Permission.bluetooth,
        Permission.bluetoothConnect,
        Permission.bluetoothScan
      ].request();
    }
    await FlutterBluePlus.startScan(
        timeout: const Duration(seconds: 20), androidUsesFineLocation: false);
  }

  Stream<List<ScanResult>> get scanresult => FlutterBluePlus.scanResults;

  Future bluetoothprint() async {
    await _flutterxprintersdkPlugin.bluetoothprint(
        orderiteam: orderiteam, printerBusinessModel: printermodel);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              MaterialButton(
                onPressed: () {
                  initPlatformState();
                },
                child: Text("Xprinter init"),
              ),
              MaterialButton(
                onPressed: () {
                  connectioncheck();
                },
                child: Text("Connection Check"),
              ),
              MaterialButton(
                onPressed: () async {
                  var data = await _flutterxprintersdkPlugin
                      .xprinterconnect(printermodel);
                  print(data);

                  // //
                  var data2 = await _flutterxprintersdkPlugin.printorder(
                      orderiteam: orderiteam,
                      printerBusinessModel: printermodel);
                  print(data2);
                },
                child: Text("XPrinter Connect"),
              ),
              MaterialButton(
                onPressed: () {
                  bluetoothprint();
                },
                child: const Text("Bluetooth Printer"),
              ),
              StreamBuilder<List<ScanResult>>(
                stream: scanresult,
                builder: (c, snapshot) {
                  if (snapshot.hasData) {
                    var realdevicelist = snapshot.data!
                        .where((element) => element.device.localName != '')
                        .toList();

                    return ListView.builder(
                        shrinkWrap: true,
                        itemCount: realdevicelist.length,
                        itemBuilder: (context, index) {
                          var d = realdevicelist[index];

                          return ListTile(
                            title: Text(d.device.localName),
                            subtitle: Text(d.device.remoteId.str),
                            onTap: () async {
                              PrinterBusinessModel printermodel2 =
                                  PrinterBusinessModel(
                                      autoPrint: true,
                                      fontSize: 16,
                                      printOnCollection: 1,
                                      printOnDelivery: 1,
                                      printOnTableOrder: 1,
                                      printOnTackwayOrder: 1,
                                      printerConnection: "USB Connection",
                                      selectPrinter: "bluetooth",
                                      showOrderNoInvoice: true,
                                      bluetoothAddress: d.device.remoteId.str,
                                      bluetoothName: d.device.localName,
                                      businessname: "sdvsdvsdv",
                                      businessphone: "01932336565",
                                      ip: "192.168.0.104",
                                      businessaddress: "sdkjbvjsdhbvjhsbdv");
                              var data =
                                  await _flutterxprintersdkPlugin.printorder(
                                printerBusinessModel: printermodel2,
                                orderiteam: orderiteam,
                              );
                              print(data);
                              // setState(() async {
                              //   _device = d.device;

                              //   connect();
                              // });
                            },
                            trailing: Icon(Icons.check),
                          );
                        });
                  } else {
                    return SizedBox();
                  }
                },
              ),
              MaterialButton(
                onPressed: () async {
                  var data = await _flutterxprintersdkPlugin
                      .xprinterconnect(printermodel);

                  var data2 =
                                  await _flutterxprintersdkPlugin.printLocalOrder(
                                printerBusinessModel: printermodel,
                                orderiteam: localorderjson,
                              );
                              print(data2);


                  print(data);
                },
                child: Text("Xprinter Connect"),
              ),
              MaterialButton(
                onPressed: () async {
                  var data =
                      await _flutterxprintersdkPlugin.xprinterdisconnect();
                  print(data);
                },
                child: Text("Xprinter disconnect"),
              ),
              MaterialButton(
                onPressed: () async {
                  var data = await _flutterxprintersdkPlugin.getimagebytes(
                      orderiteam: orderiteam,
                      printerBusinessModel: printermodel);
                      print(Uint8List.fromList(data!));
                  //  ImagePrint().imageprint(data);
                  print("ascbashjcbajhsb ${data}");
                  // ImagePrintData().imageprint();
                },
                child: Text("image bytes data get"),
              ),
              MaterialButton(
                onPressed: () async {
                    await _flutterxprintersdkPlugin.getorderview(
                      orderiteam: orderiteam,
                      printerBusinessModel: printermodel);
                  //  ImagePrint().imageprint(data);
                  // print("ascbashjcbajhsb ${data}");
                },
                child: Text("start activity"),
              ),
            ],
          ),
        ),
      ),
    );
  }

  // printer business model

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
