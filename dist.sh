#!/bin/sh

zip=editorHuesos.zip

./build.sh && cd docs && markdown && cd .. && zip $zip -X9 docs/* && zip $zip -jX9 target/editorHuesos-release-1.0-SNAPSHOT.jar

