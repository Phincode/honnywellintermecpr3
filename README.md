# honnywellintermecpr3
Only for android platform,
This is a new flutter plugin to print (image and text) on honnywell pr3 printer model,
Image format(.png,.bmp) white and black....
## Setup

## Step 1
Import plugin to your pubspec file
## Step 2
add folder hsm-android-print to your android module root
## Step 3
add this to your android/settings.gradle
include ':hsm-android-print'

## Getting Started

##Commande to send to printer

Create String List like following
``` Dart
var cmd=[
  "setBold;true",--->set next printing string bold
  "setDoubleHigh;true",---->set next printing string height
  "setDoubleWide;true",---->set next printing string widht
  "write;Test news",---->print string
  "setBold;false",
  "setDoubleHigh;false",
  "setDoubleWide;false",
  "newLine;2",--->take new line
  "newLine;2",--->take new line
  "write;ok",--->print String
  "newLine;2",
  "newLine;2",
  "offset;80",--->set offset
  "width;400",--->set widht
  "heigh;400",--->set height
  "image;base64 image string"--->image to print
];
```

##use case
  Honnywellintermecpr3 n =new Honnywellintermecpr3();
  n.printGeneral("device name", "device bleutooth mac address",cmd);

## ! important !

Verify that you have printing service apk install on your android phone before exécute the code
##
you can download it here









