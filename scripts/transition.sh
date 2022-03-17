#!/bin/sh

actualLanguage=$LANG

title="Calibration"
text="Just a little more effort !"
okButton="Next"
cancelButton="Cancel"

if [ "$actualLanguage" = "fr_FR.UTF-8" ]; then
	title="Calibration"
	text="Encore un petit effort !"
	okButton="Suivant"
	cancelButton="Annuler"
fi

(echo "<h1><center>$text</center></h1><img src=\"data:"
 mimetype -b "$1"
 echo -n ";base64,"
 base64 "$1"
 echo "\">") | zenity --text-info --html --filename=/dev/stdin --width=530 --height=425 --timeout=5 --title="$title" --ok-label "$okButton" --cancel-label "$cancelButton"
