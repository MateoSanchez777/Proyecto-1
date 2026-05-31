@echo off
setlocal enabledelayedexpansion
if exist sources.txt del sources.txt
if exist sources_src.txt del sources_src.txt
for /R src %%f in (*.java) do (
    echo "%%f" >> sources.txt
)
findstr /V "pruebas" sources.txt > sources_src.txt
javac -encoding UTF-8 -d bin -cp "lib/json.jar;src" @sources_src.txt
if %ERRORLEVEL% EQU 0 (
    echo Compilacion exitosa.
) else (
    echo Error de compilacion.
)
