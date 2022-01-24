#!/bin/bash
rm -f tobii_windowId.txt
var=$(xdotool search --onlyvisible --class tobii)
while [ -z "$var" ]
do

  var=$(xdotool search --onlyvisible --class tobii)
  sleep 0.2

done

echo "$var" >tobii_windowId.txt
xdotool windowsize "$var" 100% 100%

pgrep -x "tobii_config"
var_running=$?

while [ "$var_running" = "0" ];
do
  sleep 2
  pgrep -x "tobii_config"
  var_running=$?
done

sleep 2

sh ~/InterAACtionBox_Interface-linux/bin/scripts/transition.sh ~/InterAACtionBox_Interface-linux/bin/images/transition.gif

sh ~/InterAACtionBox_Interface-linux/bin/scripts/interAACtionGaze_windowId.sh

exit 0
