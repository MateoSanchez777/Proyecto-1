@echo off
cd src
javac -encoding UTF-8 -d ../bin -cp "../lib/json.jar;." @sources.txt
echo done
