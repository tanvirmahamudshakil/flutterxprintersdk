import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutterxprintersdk/Model/printerbusinessmodel.dart';
import 'package:flutterxprintersdk/flutterxprintersdk.dart';
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

  Future<void> connectioncheck() async {
    var data = await _flutterxprintersdkPlugin.connectioncheck();
    print(data);
  }

  Connectiontype connectiontype = Connectiontype.bluetooth;

  Future<void> printerconnect() async {
    var data =
        await _flutterxprintersdkPlugin.xprinterconnect(printermodel);
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
    await _flutterxprintersdkPlugin.bluetoothprint(orderiteam: orderiteam,printerBusinessModel: printermodel);
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
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

               var data = await  _flutterxprintersdkPlugin.xprinterconnect(printermodel);
               print(data);
               
               //
               // var data2 =await _flutterxprintersdkPlugin.printorder(
               //                    businessaddress: "sdbvsbvsdv",
               //                    businessname: "dvjsvsjbhvsdv",
               //                    businessphone: "6516515151",
               //                    connectiontype: Connectiontype.usb,
               //                    fontsize: 30,
               //                    orderiteam: orderiteam,
               //                    ip: "192.168.0.104"
               //                  );
               // print(data);
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
                            var data =
                                await _flutterxprintersdkPlugin.printorder(
                                  printerBusinessModel: printermodel,
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
                var data = await  _flutterxprintersdkPlugin.xprinterconnect(printermodel);
                print(data);

              },
              child: Text("Xprinter Connect"),
            ),
             MaterialButton(
              onPressed: () async {
                var data = await  _flutterxprintersdkPlugin.xprinterdisconnect();
                print(data);

              },
              child: Text("Xprinter disconnect"),
            ),
          ],
        ),
      ),
    );
  }



  // printer business model

  PrinterBusinessModel printermodel = PrinterBusinessModel(
    autoPrint: true,
    fontSize: 16,
    printOnCollection: 2,
    printOnDelivery: 1,
    printOnTableOrder: 1,
    printOnTackwayOrder: 1,
    printerConnection: "ip",
    selectPrinter: "bluetooth",
    showOrderNoInvoice: true,
    bluetoothAddress: "86:67:7A:1E:0D:34",
    bluetoothName: "dsvsdvsd",
    businessname: "sdvsdvsdv",
    businessphone: "01932336565",
    ip: "192.168.0.104"
  );


}
