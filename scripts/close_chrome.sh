#!/bin/bash
while [ ! -e ~/Téléchargements/close161918.txt ] && [ ! -e ~/Downloads/close161918.txt ]
do
    sleep 1
done
rm -f ~/Téléchargements/close161918.txt
rm -f ~/Downloads/close161918.txt
pkill chrome
