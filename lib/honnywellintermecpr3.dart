
import 'dart:async';

import 'package:flutter/services.dart';

class Honnywellintermecpr3{
  static const MethodChannel _channel =
      const MethodChannel('honnywellintermecpr3');



  ///send device name and mac adress to native code
  ///String deviceName, bleutooth device name
  ///String deviceBleutoothMacAdress,bleutooth device name
  ///String image to print in b64 in png or bmp format

   Future<dynamic> printImage(String deviceName,String deviceBleutoothMacAdress,String imageb64) async {
    final String res = await _channel.invokeMethod('printImg',{
      "deviceName":deviceName,
      "deviceBleutoothMacAdress":deviceBleutoothMacAdress,
      "imageb64":imageb64
    });
    return res;
  }

  Future<dynamic> printGeneral(String deviceName,String deviceBleutoothMacAdress,List cmd) async {
    final String res = await _channel.invokeMethod('printGeneral',{
      "deviceName":deviceName,
      "deviceBleutoothMacAdress":deviceBleutoothMacAdress,
      "cmd":cmd
    });
    return res;
  }




}
