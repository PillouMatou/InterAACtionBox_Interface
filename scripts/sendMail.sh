#!/bin/sh

firstname=$1
lastname=$2
data=$3
subject=$4
to=$5

#Send message to user to tell him that we have received his message
echo "Bonjour,
	
Merci d'avoir contacté notre support.
Nous avons bien reçu votre message !
	
Cordialement,

	L'équipe d'interAACtionBox" | mail -s "Contact support interAACtionBox" "$5"
	
	
#Send message to the dev mail
echo "Nom de l'utilisateur : $2
Prénom de l'utilisateur : $1
Mail de l'utilisateur : $5
Message : $3" | mail -s "$4" "contact.interaactionBox@gmail.com"

exit
