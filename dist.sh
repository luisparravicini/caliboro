#!/bin/sh

dist=dist

[ -d "$dist" ] && rm -rf "$dist"
mkdir "$dist"
./build.sh && cp target/caliboro-*.exe target/caliboro-*-release.jar dist
