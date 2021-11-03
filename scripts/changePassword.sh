#!/bin/bash
echo -e $(zenity --forms --title="Changer de mot de passe" --text="Mot de passe pour $USER" --separator="\n" --add-password="Ancien mot de passe" --add-password="Nouveau mot de passe" --add-password="Confirmez le mot de passe") | passwd
