import 'package:flutter_test/flutter_test.dart';
import 'package:flutterxprintersdk/flutterxprintersdk.dart';
import 'package:flutterxprintersdk/flutterxprintersdk_platform_interface.dart';
import 'package:flutterxprintersdk/flutterxprintersdk_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterxprintersdkPlatform
    with MockPlatformInterfaceMixin
    implements FlutterxprintersdkPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterxprintersdkPlatform initialPlatform = FlutterxprintersdkPlatform.instance;

  test('$MethodChannelFlutterxprintersdk is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterxprintersdk>());
  });

  test('getPlatformVersion', () async {
    Flutterxprintersdk flutterxprintersdkPlugin = Flutterxprintersdk();
    MockFlutterxprintersdkPlatform fakePlatform = MockFlutterxprintersdkPlatform();
    FlutterxprintersdkPlatform.instance = fakePlatform;

    expect(await flutterxprintersdkPlugin.getPlatformVersion(), '42');
  });
}
