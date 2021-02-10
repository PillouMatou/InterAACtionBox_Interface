 #!/bin/bash
rm -f gazeplay_windowId.txt
var=$(xdotool search --onlyvisible --class google-chrome)
while [ -z "$var" ]
do

	var=$(xdotool search --onlyvisible --class google-chrome)
	sleep 0.2

done
 
echo "$var" > gazeplay_windowId.txt
cat gazeplay_windowId.txt
exit 0
