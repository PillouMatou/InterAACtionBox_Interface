#!/bin/sh

set -e

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

export JAVA_CMD="java -cp \"$CLASSPATH\" ${JAVA_OPTS} Main"

echo "Executing command line: $JAVA_CMD"

${JAVA_CMD}
