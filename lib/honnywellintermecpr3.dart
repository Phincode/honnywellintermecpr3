
import 'dart:async';

import 'package:flutter/services.dart';

class Honnywellintermecpr3{
  static const MethodChannel _channel =
      const MethodChannel('honnywellintermecpr3');



  ///send device name and mac adress to native code
  ///String deviceName, bleutooth device name
  ///String deviceBleutoothMacAdress,bleutooth device name
  ///List cmd , commande to send to printer
  ///
  Future<dynamic> printGeneral(String deviceName,String deviceBleutoothMacAdress,List cmd) async {
    final String res = await _channel.invokeMethod('printGeneral',{
      "deviceName":deviceName,
      "deviceBleutoothMacAdress":deviceBleutoothMacAdress,
      "cmd":cmd
    });
    return res;
  }




}
