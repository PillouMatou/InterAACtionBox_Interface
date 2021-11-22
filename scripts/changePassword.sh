#!/bin/bash
echo -e $(echo -e $(zenity --forms --title="Changer de mot de passe" --text="Mot de passe pour $USER" --separator="\n" --add-password="Ancien mot de passe" --add-password="Nouveau mot de passe" --add-password="Confirmez le mot de passe") | sed ':a;N;$!ba;s/\n/\\n/g') | passwd
