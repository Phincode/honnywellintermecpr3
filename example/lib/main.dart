import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:honnywellintermecpr3/honnywellintermecpr3.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
String b64="iVBORw0KGgoAAAANSUhEUgAABkAAAAZAAQMAAAAbwhzkAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAGUExURQAAAP7+/soH9D0AAAYwSURBVHja7dyxcYNAEAVQPA4UugRKoTRcGqW4BIcKNMKBcABzXu8JhIXn/UwSJ/Zt/Oea8Z+kAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAXkiyGdTn77iaHt79nLHa1oQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEJA9IC+ZVvolhHS3b88hpE+130FAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQP4AMmQK7vNpruVme80OppxAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQECOCeluH1PdeBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQkGND4pvaQUBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBADgYJE0OG+TTh0TggICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB7QqqSgpS78TUBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQF5LGRF4h2MjwoICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICMhGkPI0izvTP6Ju/IosLnk/b1/yBwEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB+RVyz1+mdjDlEu6gW4wKAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKyB6Scc1hp76MdjCGk3I0f5jsAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHZBRJ348PhYkhNrb6rWTIICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICMi2kB8yPfTRJFKepnz07fbjdX40tT4QEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEJCtICuSqtWndjDllHotCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAjIRpDPpj797B+umSOv08PvGcj0sQUBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAdkD8pJppV/mkMXRYT5VuRtf3kFXU8kHAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQF5DGQoltbbImQxzYpMkMVN7SAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAeDnMZEype8p46CgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA8AeSem9pr0oKAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgOwJSU0zh3ynW7yvOM17ceRurAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICMi2kKr00dFTZgdTrjU7AAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB2RhyvICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICArMkXJlylJdANIpEAAAAASUVORK5CYII=";

var cmd=[
  "setBold;true",
  "setDoubleHigh;true",
  "setDoubleWide;true",
  "write;Test news",
  "setBold;false",
  "setDoubleHigh;false",
  "setDoubleWide;false",
  "newLine;2",
  "newLine;2",
  "write;ok",
  "newLine;2",
  "newLine;2",
  "offset;80",
  "width;400",
  "heigh;400",
  "image;iVBORw0KGgoAAAANSUhEUgAABkAAAAZAAQMAAAAbwhzkAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAGUExURQAAAP7+/soH9D0AAAYwSURBVHja7dyxcYNAEAVQPA4UugRKoTRcGqW4BIcKNMKBcABzXu8JhIXn/UwSJ/Zt/Oea8Z+kAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAXkiyGdTn77iaHt79nLHa1oQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEJA9IC+ZVvolhHS3b88hpE+130FAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQP4AMmQK7vNpruVme80OppxAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQECOCeluH1PdeBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQkGND4pvaQUBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBADgYJE0OG+TTh0TggICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB7QqqSgpS78TUBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQF5LGRF4h2MjwoICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICMhGkPI0izvTP6Ju/IosLnk/b1/yBwEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB+RVyz1+mdjDlEu6gW4wKAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKyB6Scc1hp76MdjCGk3I0f5jsAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHZBRJ348PhYkhNrb6rWTIICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICMi2kB8yPfTRJFKepnz07fbjdX40tT4QEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEJCtICuSqtWndjDllHotCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAjIRpDPpj797B+umSOv08PvGcj0sQUBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAdkD8pJppV/mkMXRYT5VuRtf3kFXU8kHAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQF5DGQoltbbImQxzYpMkMVN7SAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAeDnMZEype8p46CgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA8AeSem9pr0oKAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgOwJSU0zh3ynW7yvOM17ceRurAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICMi2kKr00dFTZgdTrjU7AAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB2RhyvICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICArMkXJlylJdANIpEAAAAASUVORK5CYII=",
];

  @override
  void initState() {
    super.initState();

  }



  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child:RaisedButton(
            onPressed: (){
              Honnywellintermecpr3 n =new Honnywellintermecpr3();
              n.printGeneral("PR3", "B8:69:C2:25:4F:1B",cmd);
            },
            child: Text('open'),
          ),
        ),
      ),
    );
  }
}
