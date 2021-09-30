#!/bin/bash
rm -f teamviewer_windowId.txt
var=$(xdotool search --onlyvisible --class teamviewer)
while [ -z "$var" ]
do

  var=$(xdotool search --onlyvisible --class teamviewer)
  sleep 0.2

done

echo "$var" >teamviewer_windowId.txt
xdotool windowsize "$var" 100% 100%
exit 0
