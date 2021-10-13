#!/bin/bash
while [ ! -e ~/Téléchargements/close.txt ] && [ ! -e ~/Downloads/close.txt ]
do
    sleep 1
done
pkill chrome
rm -f ~/Téléchargements/close.txt
rm -f ~/Downloads/close.txt
