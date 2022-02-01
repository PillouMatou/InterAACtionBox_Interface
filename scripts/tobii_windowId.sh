#!/bin/bash

zenity --info \
	--title="InterAACtionBox" \
  	--text="Eye Tracker non connecté ou non calibré" \
	--width=300 \
	--height=100 \
	--ok-label Quitter \
  	--extra-button Suivant  
start=$?
if [ $start -eq 0 ];
then
	exit 0
else

	zenity --question \
  		--title="InterAACtionBox" \
  		--text="Avez-vous branché votre Eye Tracker ?" \
  		--width=300 \
  		--height=100 \
  		--ok-label="Oui" \
  		--cancel-label="Non"
  		
  	if [ $? -eq 1 ];
  	then
  		zenity --info \
  		--title="InterAACtionBox" \
  		--text="Veuillez brancher votre Eye Tracker !" \
  		--width=300 \
  		--height=100 
  	else
  		zenity --question \
  			--title="InterAACtionBox" \
  			--text="Voulez-vous faire une première calibration ?" \
  			--width=300 \
  			--height=100 \
  			--ok-label="Oui" \
  			--cancel-label="Non"
  			
  		if [ $? -eq 1 ];
  		then
  			exit 0
  		else
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
			var_runing=$?

			while [ "$var_runing" = "0" ];
			do
				sleep 2
				pgrep -x "tobii_config"
				var_runing=$?
			done	

			sleep 2

			sh ~/InterAACtionBox_Interface-linux/bin/scripts/transition.sh ~/InterAACtionBox_Interface-linux/bin/images/transition.gif

			sh ~/InterAACtionBox_Interface-linux/bin/scripts/interAACtionGaze-calibration_windowId.sh
  		fi
  	fi	
fi

exit 0
