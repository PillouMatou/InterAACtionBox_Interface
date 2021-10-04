#!/bin/sh  
  
set -e  
  
MAIN_JAR_FILE=interaactionBoxOs.jar  
  
WORKING_DIR=$(pwd)  
  
echo "WORKING_DIR = ${WORKING_DIR}"  
  
SCRIPT_DIR=$(dirname $0)  
  
echo "SCRIPT_DIR = ${SCRIPT_DIR}"  
  
LIB_DIR=${SCRIPT_DIR}/../lib  

FILE=${SCRIPT_DIR}/configuration.conf

gazePlaySave="empty"

if [ ! -f ${FILE} ]; then 
 FILEPATH=$(ls ~ | grep "gazeplay-linux.*")$(echo "")
 if [ ! "$FILEPATH" = "" ]; then
  echo "../../${FILEPATH}">${FILE}
 fi;
fi;


if [ -f "$FILE" ]; then
    echo "exists"
    gazePlaySave=$(head -n 1 "${FILE}")
    if [ -d "$gazePlaySave" ]; then
      
      export JAVA_CMD="${WORKING_DIR}/../lib/jre/bin/java -cp \"$CLASSPATH\" ${JAVA_OPTS} main.Main $gazePlaySave"

      echo "Executing command line: $JAVA_CMD"

      ${JAVA_CMD}
      
      exit
    fi
fi

while [ ! -d "$gazePlaySave" ] 
do
read -p "Entrez le chemin menant au dossier d'installation de GazePlay: " gazePlaySave
if [ ! -d "$gazePlaySave" ]; then
 echo "Erreur: \"$gazePlaySave\" n'est pas un dossier valide."
fi
done

echo "$gazePlaySave" > configuration.conf
  
export JAVA_CMD="${WORKING_DIR}/../lib/jre/bin/java -cp \"$CLASSPATH\" ${JAVA_OPTS} main.Main $gazePlaySave"

echo "Executing command line: $JAVA_CMD"

${JAVA_CMD}
