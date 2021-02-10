 #!/bin/bash
rm -f tobii_windowId.txt
var=$(xdotool search --onlyvisible --class tobii)
while [ -z "$var" ]
do

	var=$(xdotool search --onlyvisible --class tobii)
	sleep 0.2

done
 
echo "$var" > tobii_windowId.txt
cat tobii_windowId.txt
xdotool windowsize $var 100% 100%
exit 0
