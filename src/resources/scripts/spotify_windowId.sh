#!/bin/bash
rm -f spotify_windowId.txt
var=$(xdotool search --onlyvisible --class spotify)
while [ -z "$var" ]; do

  var=$(xdotool search --onlyvisible --class spotify)
  sleep 0.2

done

echo "$var" >spotify_windowId.txt
cat spotify_windowId.txt
xdotool windowsize "$var" 100% 100%
exit 0
