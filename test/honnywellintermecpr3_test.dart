import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:honnywellintermecpr3/honnywellintermecpr3.dart';

void main() {
  const MethodChannel channel = MethodChannel('honnywellintermecpr3');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {

  });
}
