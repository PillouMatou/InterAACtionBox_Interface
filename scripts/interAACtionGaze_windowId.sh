#!/bin/bash
rm -f interaactionGaze_windowId.txt
var=$(xdotool search --onlyvisible --name InteraactionGaze)
if [ -z "$var" ] || [ "$var" == "" ]
then
  sh ~/InterAACtionGaze/bin/interAACtionGaze-linux.sh
else
  echo "$var" >interaactionGaze_windowId.txt
  xdotool windowactivate  "$var"
fi
exit 0
