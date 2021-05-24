
import 'dart:async';

import 'package:flutter/services.dart';

class Honnywellintermecpr3 {
  static const MethodChannel _channel =
      const MethodChannel('honnywellintermecpr3');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  ///send device name and mac adress to native code
  ///String deviceName, bleutooth device name
  ///String deviceBleutoothMacAdress,bleutooth device name
  ///String image to print in b64 in png or bmp format

  static Future<dynamic> print(String deviceName,String deviceBleutoothMacAdress,String imageb64) async {
    final String res = await _channel.invokeMethod('setprinterdetails',{
      "deviceName":deviceName,
      "deviceBleutoothMacAdress":deviceBleutoothMacAdress,
      "imageb64":imageb64
    });
    return res;
  }


}
