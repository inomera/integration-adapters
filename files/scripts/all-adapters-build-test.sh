#!/bin/bash
exec >build.output 2>&1

cd ../../
pwd
echo "$pwd"

for lib in *; do
    if [[ "$lib" == *adapter ]]; then
      echo "$lib"
      ./gradlew :"$lib":build
      ./gradlew :"$lib":publishMavenJavaPublicationToMavenLocal
    fi
done