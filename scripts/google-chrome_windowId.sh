#!/bin/bash
rm -f chrome_windowId.txt
var=$(xdotool search --onlyvisible --class google-chrome)
while [ -z "$var" ]
do

  var=$(xdotool search --onlyvisible --class google-chrome)
  sleep 0.2

done

echo "$var" >google-chrome_windowId.txt
exit 0
