#!/bin/sh

rm -r ./build
mkdir ./build
find ./src -name "*.java" -print | xargs javac -d ./build
cp -r ./img ./build/
