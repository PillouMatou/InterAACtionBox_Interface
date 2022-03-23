#!/bin/bash

headGrep=$(ps aux | grep "interAACtionGaze.jar" | head -n 1 | awk '{print $12}')
tailGrep=$(ps aux | grep "interAACtionGaze.jar" | tail -n 1 | awk '{print $12}')

if [ "$headGrep" = "$tailGrep" ];
then
	echo "false" > gazeRunning.txt
else
	echo "true" > gazeRunning.txt
	kill $(ps aux | grep "interAACtionGaze.jar" | head -n 1 | awk '{print $2}')
fi		

exit 0
