#!/bin/sh  
  
set -e  
  
MAIN_JAR_FILE=interaactionBoxOs.jar  
  
WORKING_DIR=$(pwd)  
  
echo "WORKING_DIR = ${WORKING_DIR}"  
  
SCRIPT_DIR=$(dirname $0)  
  
echo "SCRIPT_DIR = ${SCRIPT_DIR}"  
  
LIB_DIR=${SCRIPT_DIR}/../lib  
  
echo "LIB_DIR = ${LIB_DIR}"  
  
LIB_DIR_RELATIVE=$(realpath --relative-to="${WORKING_DIR}" "${LIB_DIR}")  
  
echo "LIB_DIR_RELATIVE = ${LIB_DIR_RELATIVE}"  
  
CLASSPATH=$(find ./$LIB_DIR_RELATIVE -name "*.jar" | sort | tr '\n' ':')  
  
export JAVA_HOME=${LIB_DIR}/jre  
  
echo "JAVA_HOME = ${JAVA_HOME}"  
  
export PATH=${JAVA_HOME}/bin:${PATH}  
  
echo "PATH = ${PATH}"

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
    echo "$gazePlaySave"
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
