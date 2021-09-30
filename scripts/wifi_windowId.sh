#!/bin/bash
rm -f wifi_windowId.txt
var=$(xdotool search --onlyvisible --class gnome-control-center)
while [ -z "$var" ]
do

  var=$(xdotool search --onlyvisible --class gnome-control-center)
  sleep 0.2

done

echo "$var" >wifi_windowId.txt
xdotool windowsize "$var" 100% 100%
exit 0
