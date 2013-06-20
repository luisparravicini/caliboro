#!/bin/sh

zip=caliboro.zip

[ -f "$zip" ] && rm "$zip"
./build.sh && cd docs && markdown && cd .. && zip $zip -X9 docs/* && zip $zip -jX9 target/caliboro-*

