#!/bin/bash
if [ -z "$1" ]
  then
    echo "Version argument missing"
    exit 1
fi
./gradlew -Pversion="$1" clean build -x test publishToMavenLocal bintrayUpload
