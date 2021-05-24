package com.phincode.honnywellintermecpr3;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import static androidx.core.content.ContextCompat.startActivity;

/** Honnywellintermecpr3Plugin */

public class Honnywellintermecpr3Plugin  implements FlutterPlugin, MethodCallHandler{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  Context c;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "honnywellintermecpr3");
    channel.setMethodCallHandler(this);
    c=flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("printImg")) {

      String deviceName = call.argument("deviceName");
      String deviceBleutoothMacAdress = call.argument("deviceBleutoothMacAdress");
      String imageb64 = call.argument("imageb64");
      Intent intent = new Intent(this.c,PrintActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.putExtra("deviceName",deviceName);
      intent.putExtra("deviceBleutoothMacAdress",deviceBleutoothMacAdress);
      intent.putExtra("imageb64",imageb64);
      startActivity(this.c,intent,null);



    } else if(call.method.equals("printGeneral")){
      String deviceName = call.argument("deviceName");
      String deviceBleutoothMacAdress = call.argument("deviceBleutoothMacAdress");
      ArrayList<String> commande = call.argument("cmd");
      Log.d ("cmd",commande.toString());
      Intent intent = new Intent(this.c,PrintActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.putExtra("deviceName",deviceName);
      intent.putExtra("deviceBleutoothMacAdress",deviceBleutoothMacAdress);
      intent.putExtra("cmd",commande);
      startActivity(this.c,intent,null);

    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);

  }



}
