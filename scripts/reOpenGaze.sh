#!/bin/bash

File=./gazeRunning.txt

read -r line <$File

if [ "$line" = "true" ]; then	

	sh ~/InterAACtionGaze/bin/interAACtionGaze-linux.sh
	
fi
	
exit 0
