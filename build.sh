#!/bin/bash

cd html-app
grunt build
rm -Rf ../server/resources/html-app
cp -R targets/public ../server/resources/html-app
cd ../server
lein uberjar
cd ..
cp  server/target/gratefulplace-0.1.0-SNAPSHOT-standalone.jar infrastructure/ansible/files/gp2.jar
