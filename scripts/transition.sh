#!/bin/sh

(echo "<h1><center>Encore un petit effort ^^ !</center></h1><img src=\"data:"
 mimetype -b "$1"
 echo -n ";base64,"
 base64 "$1"
 echo "\">") | zenity --text-info --html --filename=/dev/stdin --width=530 --height=425 --timeout=5 --title="Calibration"
