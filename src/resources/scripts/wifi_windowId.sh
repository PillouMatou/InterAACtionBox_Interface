#!/bin/bash
rm -f wifi_windowId.txt
var=$(xdotool search --onlyvisible --class nm-connection-editor)
while [ -z "$var" ]; do

  var=$(xdotool search --onlyvisible --class nm-connection-editor)
  sleep 0.2

done

echo "$var" >wifi_windowId.txt
cat wifi_windowId.txt
xdotool windowsize $var 100% 100%
exit 0
